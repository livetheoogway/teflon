package com.teflon.task.framework;

import com.teflon.task.framework.core.*;
import com.teflon.task.framework.core.meta.TaskStat;
import com.teflon.task.framework.error.TeflonError;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * This is the main executor class.
 * This class is responsible for orchestrating the execution of a {@link Task} instance
 * 1. Initiate a {@link Task}, i.e. all its responsible actors
 * 2. Regular update callbacks (TaskStatus)
 * 3. Regular check if task has been cancelled
 * 4. Handle exceptions from any of the Actors
 * 5. Close Abort actors according to the state of the execution
 * The execution will continue until Source produces some inputs.
 * Execution stops the moment the Source produces null or Empty list of Inputs
 * <p>
 * PS: All Actors involved {@link Source}, {@link Interpreter}, {@link Sink} need to be threadsafe
 *
 * @author tushar.naik
 * @version 1.0  10/08/17 - 6:05 PM
 */
@Slf4j
public class TaskExecutor<Input, Progress, Output> {

    /* default batch size */
    private static final int DEFAULT_BATCH_SIZE = 100;

    /* batch size when update callbacks are called + cancellation checked */
    private int batchSize = DEFAULT_BATCH_SIZE;

    /* Source of Inputs */
    private Source<Input, Progress> source;

    /* Source input interpreter */
    private Interpreter<Input, Output> interpreter;

    /* Sink for Inputs */
    private Sink<Output> sink;

    /* count of Inputs sent to Sink, during the execution */
    private long sinkCount = 0;

    /* total Inputs during the execution */
    private long total = 0;

    /* stats collector */
    private TaskStat<Progress> taskStat;

    /**
     * @param source      impl of a Source and produces Input or null
     * @param interpreter interpret Input to Output
     * @param sink        Sink where Output will be pushed
     */
    @Builder
    public TaskExecutor(Source<Input, Progress> source, Interpreter<Input, Output> interpreter,
                        Sink<Output> sink, int batchSize) {
        this.source = source;
        this.interpreter = interpreter;
        this.sink = sink;
        this.batchSize = batchSize <= 0 ? DEFAULT_BATCH_SIZE : batchSize;
        this.taskStat = new TaskStat<>(total, sinkCount);
    }

    /**
     * for a given input, initiates the Source, Interpreter and Sink.
     * Streams every Input, Interprets it to an Output, and then sinks the final Output
     *
     * @param task           task to be executed
     * @param statusCallback something that consumes regular updates.
     *                       1. {@link StatusCallback#onInit(Task, TaskStat)} is called during init
     *                       2. {@link StatusCallback#statusCallback(Task, TaskStat)} when the number of inputs reaches batchSize
     *                       3. {@link StatusCallback#isCancelled(Task, TaskStat)} check cancellation ( when the number of inputs reaches batchSize)
     *                       4. {@link StatusCallback#onComplete(Task, TaskStat)} when execution completes
     *                       5. {@link StatusCallback#onError(Task, TaskStat, Throwable)} when Execution results in an Exception
     * @return <code>true</code> if successfully executed the task
     * @throws TeflonError any error during task execution
     */
    public boolean initiate(Task task, StatusCallback statusCallback) throws TeflonError {
        int lastBatchCount = -1;

        log.info("Starting to populate all values from source...");
        taskStat.start();
        try {
            /* initiate all actors */
            source.init(task);
            interpreter.init(task);
            sink.init(task);
            statusCallback.onInit(task, taskStat);

            /* execute task */
            executeTask(task, statusCallback, lastBatchCount, taskStat);

            /* close all actors */
            source.close();
            interpreter.close();
            sink.close();

            taskStat.end();
            /* will return true unless the task was cancelled */
            return !statusCallback.isCancelled(task, taskStat);
        } catch (ClassCastException e) {
            log.error("!!! \\\\_(- -)_//  Incompatible type of Actors while running task. Aborting.. ", e);
            source.abort();
            interpreter.abort();
            sink.abort();
            taskStat.end();
            statusCallback.onError(task, taskStat, e);
            return false;
        } catch (Throwable e) {
            log.error("!!! \\\\_(- -)_//  Ran into problems while running task. Aborting.. ", e);
            source.abort();
            interpreter.abort();
            sink.abort();
            taskStat.end();
            statusCallback.onError(task, taskStat, e);
            return false;
        }
    }

    public boolean resume(Task task, StatusCallback statusCallback, TaskStat taskStat) throws TeflonError {
        int lastBatchCount = -1;
        log.info("Resuming task:{}", task);
        try {
            /* initiate all actors */
            source.resume(task, taskStat);
            statusCallback.onResume(task, taskStat);

            sinkCount = taskStat.getCountOutputSunk();
            total = taskStat.getCountTotal();
            /* execute */
            executeTask(task, statusCallback, lastBatchCount, taskStat);

            /* close all actors */
            source.close();
            interpreter.close();
            sink.close();

            taskStat.end();
            /* will return true unless the task was cancelled */
            return !statusCallback.isCancelled(task, taskStat);
        } catch (ClassCastException e) {
            log.error("!!! \\\\_(- -)_//  Incompatible type of Actors while running task. Aborting.. ", e);
            source.abort();
            interpreter.abort();
            sink.abort();
            taskStat.end();
            statusCallback.onError(task, taskStat, e);
            return false;
        } catch (Throwable e) {
            log.error("!!! \\\\_(- -)_//  Ran into problems while running task. Aborting.. ", e);
            source.abort();
            interpreter.abort();
            sink.abort();
            taskStat.end();
            statusCallback.onError(task, taskStat, e);
            return false;
        }
    }

    private void executeTask(Task task, StatusCallback statusCallback, int lastBatchCount,
                             TaskStat<Progress> taskStat) throws Exception {
        List<Input> inputs;
        do {
            SourceInputs<Input, Progress> sourceInputs = source.getInput();
            if (sourceInputs == null || sourceInputs.getInputs() == null) {
                break;
            }
            inputs = sourceInputs.getInputs();
            taskStat.setTaskProgress(sourceInputs.getProgress());
            total += inputs.size();
            List<Output> interpretedInputs = interpreter.interpret(inputs);
            if (interpretedInputs != null && !interpretedInputs.isEmpty()) {
                sink.sink(interpretedInputs);
                sinkCount += interpretedInputs.size();
                taskStat.setCountTotal(total);
                taskStat.setCountOutputSunk(sinkCount);
                /* call updates consumer only if the number of interpreted inputs were > 1 (Prevents unnecessary noise)  */
                if (interpretedInputs.size() > 1) {
                    statusCallback.statusCallback(task, taskStat);
                }
            }
            if (total / batchSize > lastBatchCount) {
                lastBatchCount = (int) (total / batchSize);
                log.info("Task:{} total:{} stat:{}", task.name(), total, taskStat);
                statusCallback.statusCallback(task, taskStat);
                /* this is where we break the if the task was cancelled */
                if (statusCallback.isCancelled(task, taskStat)) {
                    break;
                }
            }
        } while (!inputs.isEmpty());

        statusCallback.onComplete(task, taskStat);
        log.info("Task of {} inputs from source. Successfully executed in {}. Stats-{}", total, taskStat.getElapsedTime(), taskStat);
        log.info("Closing sources and sink ..");
    }

    @Override
    public String toString() {
        return "TaskExecutor[" + "batchSize:" + batchSize +
                ", source:" + source +
                ", interpreter:" + interpreter +
                ", sink:" + sink +
                ", count:" + sinkCount +
                ", total:" + total +
                ", taskStat:" + taskStat +
                ']';
    }
}


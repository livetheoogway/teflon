package com.teflon.task.framework;

import com.teflon.task.framework.core.*;
import com.teflon.task.framework.core.meta.TaskStat;
import com.teflon.task.framework.error.ErrorCode;
import com.teflon.task.framework.error.TeflonError;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

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
public class TaskExecutor<Input, Output> {

    /* default batch size */
    private static final int DEFAULT_BATCH_SIZE = 100;

    /* batch size when update callbacks are called + cancellation checked */
    private int batchSize = DEFAULT_BATCH_SIZE;

    /* Source of Inputs */
    private Source<Input> source;

    /* Source input interpreter */
    private Interpreter<Input, Output> interpreter;

    /* Sink for Inputs */
    private Sink<Output> sink;

    /* count of Inputs sent to Sink, during the execution */
    private long count = 0;

    /* total Inputs during the execution */
    private long total = 0;
    private TaskStat taskStat;

    /**
     * @param source      impl of a Source and produces Input or null
     * @param interpreter interpret Input to Output
     * @param sink        Sink where Output will be pushed
     */
    @Builder
    public TaskExecutor(Source<Input> source, Interpreter<Input, Output> interpreter,
                        Sink<Output> sink, int batchSize) {
        this.source = source;
        this.interpreter = interpreter;
        this.sink = sink;
        this.batchSize = batchSize <= 0 ? DEFAULT_BATCH_SIZE : batchSize;
        this.taskStat = new TaskStat(total, count);
    }

    /**
     * for a given input, initiates the Source, Interpreter and Sink.
     * Streams every Input, Interprets it to an Output, and then sinks the final Output
     *
     * @param task            task to be executed
     * @param updatesConsumer something that consumes regular updates. consumer is called when the number of inputs reaches batch size, or when
     * @param isCancelled     return true if the task was cancelled externally. Execution will stop immediately (during the next batch)
     * @return <code>true</code> if successfully executed the task
     * @throws TeflonError any error during task execution
     */
    public boolean initiate(Task task, Consumer<TaskStat> updatesConsumer,
                            BooleanSupplier isCancelled) throws TeflonError {
        int lastBatchCount = -1;

        log.info("Starting to populate all values from source...");
        taskStat.start();
        try {
            source.init(task);
            interpreter.init(task);
            sink.init(task);
            List<Input> inputs;
            do {
                inputs = source.getInput();
                if (inputs == null) {
                    break;
                }
                total += inputs.size();
                List<Output> interpretedInputs = interpreter.interpret(inputs);
                if (interpretedInputs != null && !interpretedInputs.isEmpty()) {
                    sink.sink(interpretedInputs);
                    count += interpretedInputs.size();
                    taskStat.setCountTotal(total);
                    taskStat.setCountOutputSinked(count);
                    /* call updates consumer only if the number of interpreted inputs were > 1 (Prevents unnecessary noise)  */
                    if (interpretedInputs.size() > 1) {
                        updatesConsumer.accept(taskStat);
                    }
                }
                if (total / batchSize > lastBatchCount) {
                    lastBatchCount = (int) (total / batchSize);
                    log.info("Task:{} total:{} stat:{}", task.name(), total, taskStat);
                    updatesConsumer.accept(taskStat);
                    /* this is where we break the if the task was cancelled */
                    if (isCancelled.getAsBoolean()) {
                        break;
                    }
                }
            } while (!inputs.isEmpty());

            taskStat.end();
            updatesConsumer.accept(taskStat);
            log.info("Task of {} inputs from source. Successfully executed in {}. Stats-{}", total, taskStat.getElapsedTime(), taskStat);
            log.info("Closing sources and sink ..");

            /* close all actors */
            source.close();
            interpreter.close();
            sink.close();

            /* will return true unless the task was cancelled */
            return !isCancelled.getAsBoolean();
        } catch (ClassCastException e) {
            log.error("!!! \\\\_(- -)_//  Incompatible type of Actors while running task. Aborting.. ", e);
            taskStat.end();
            updatesConsumer.accept(taskStat);
            source.abort();
            interpreter.abort();
            sink.abort();
            throw new TeflonError(ErrorCode.INCOMPATIBLE_TYPES_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("!!! \\\\_(- -)_//  Ran into problems while running task. Aborting.. ", e);
            taskStat.end();
            updatesConsumer.accept(taskStat);
            source.abort();
            interpreter.abort();
            sink.abort();
            throw TeflonError.propagate(e);
        }
    }
}


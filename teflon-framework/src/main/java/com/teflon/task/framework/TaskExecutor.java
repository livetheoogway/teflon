package com.teflon.task.framework;

import com.google.common.collect.Lists;
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
 * @author tushar.naik
 * @version 1.0  10/08/17 - 6:05 PM
 */
@Slf4j
public class TaskExecutor<Input, Output> {

    private int batchSize = 100;
    private Source<Input> source;
    private Interpreter<Input, Output> interpreter;
    private Sink<Output> sink;
    private long count = 0;
    private long total = 0;
    private TaskStat taskStat;

    /**
     * @param source      impl of a Source, which inits with an Init, and produces Input or null
     * @param interpreter interpret Input to Output
     * @param sink        Sink where Output will be pushed
     */
    @Builder
    public TaskExecutor(Source<Input> source, Interpreter<Input, Output> interpreter,
                        Sink<Output> sink, int batchSize) {
        this.source = source;
        this.interpreter = interpreter;
        this.sink = sink;
        this.batchSize = batchSize == 0 ? 1 : batchSize;
        this.taskStat = new TaskStat(total, count);
    }

    /**
     * for a given input, inits the Source and Sink.
     * Streams every Input, Interprets it to an Output, and then sinks the final Output
     *
     * @param task            task to be initiated
     * @param updatesConsumer something that consumes regular updates. consumer is called when the number of inputs reaches batch size
     * @param isCancelled     return true if the task was cancelled externally. Execution will stop immediately (during the next batch)
     * @return <code>true</code> if successfully executed the task
     * @throws TeflonError any error during task execution
     */
    public boolean initiate(Task task, Consumer<TaskStat> updatesConsumer,
                            BooleanSupplier isCancelled) throws TeflonError {
        log.info("Starting to populate all values from source...");
        taskStat.start();
        try {
            source.init(task);
            interpreter.init(task);
            sink.init(task);
            Input input;
            List<Output> outputs = Lists.newArrayListWithCapacity(batchSize);
            while ((input = source.getInput()) != null && (total % batchSize != 0 || !isCancelled.getAsBoolean())) {
                total++;
                Output interpret = interpreter.interpret(input);
                if (interpret != null) {
                    outputs.add(interpret);
                    count++;
                }
                if (outputs.size() >= batchSize) {
                    sink.sink(outputs);
                    outputs.clear();
                    taskStat.setCountTotal(total);
                    taskStat.setCountOutputSinked(count);
                    updatesConsumer.accept(taskStat);
                }
                if (total % batchSize == 0) {
                    log.info("Task:{} total:{} status:{}", task, total, taskStat);
                }
            }
            if (!outputs.isEmpty()) {
                sink.sink(outputs);
                outputs.clear();
                taskStat.setCountTotal(total);
                taskStat.setCountOutputSinked(count);
                log.info("Task:{} total:{} status:{}", task, total, taskStat);
                updatesConsumer.accept(taskStat);
            }
            taskStat.end();
            log.info("Task of {} inputs from source. Successfully executed in {}. Stats-{}", total, taskStat.getTotalTime(), taskStat);
            log.info("Closing sources and sink ..");
            try {
                source.close();
            } catch (Exception e) {
                log.error("Error while closing source", e);
            }
            sink.close();

            /* will return true unless the task was cancelled */
            return !isCancelled.getAsBoolean();
        } catch (ClassCastException e) {
            log.error("!!! \\\\_(- -)_//  Ran into problems while running task. Aborting.. ", e);
            taskStat.end();
            source.abort();
            interpreter.abort();
            sink.abort();
            throw new TeflonError(ErrorCode.INCOMPATIBLE_TYPES_ERROR, e.getMessage());
        } catch (Exception e) {
            log.error("!!! \\\\_(- -)_//  Ran into problems while running task. Aborting.. ", e);
            taskStat.end();
            source.abort();
            interpreter.abort();
            sink.abort();
            throw TeflonError.propagate(e);
        }
    }
}


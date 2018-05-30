package com.teflon.task.framework.factory;

import com.teflon.task.framework.core.Interpreter;
import com.teflon.task.framework.core.Sink;
import com.teflon.task.framework.core.Source;
import com.teflon.task.framework.TaskExecutor;
import com.teflon.task.framework.core.meta.MetaInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 1:52 AM
 */
@AllArgsConstructor
@Slf4j
public class ExecutionFactory<T, V, U> implements InstanceFactory<TaskExecutor<T, V, U>> {
    private MetaInfo metaInfo;

    @Override
    public TaskExecutor<T, V, U> newInstance() {
        log.info("TaskExecutor: " + metaInfo.toString());
        return TaskExecutor.<T, V, U>builder()
                .source((Source<T, V>) metaInfo.getSourceInstanceFactory().newInstance())
                .interpreter((Interpreter<T, U>) metaInfo.getInterpreterInstanceFactory().newInstance())
                .sink((Sink<U>) metaInfo.getSinkInstanceFactory().newInstance())
                .batchSize(metaInfo.getBatchSize())
                .build();
    }
}

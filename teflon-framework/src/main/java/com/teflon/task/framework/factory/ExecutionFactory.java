package com.teflon.task.framework.factory;

import com.teflon.task.framework.core.Interpreter;
import com.teflon.task.framework.core.Sink;
import com.teflon.task.framework.core.Source;
import com.teflon.task.framework.TaskExecutor;
import com.teflon.task.framework.core.meta.MetaInfo;
import lombok.AllArgsConstructor;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 1:52 AM
 */
@AllArgsConstructor
public class ExecutionFactory<T, U> implements InstanceFactory<TaskExecutor<T, U>> {
    private MetaInfo metaInfo;

    @Override
    public TaskExecutor<T, U> newInstance() {
        return TaskExecutor.<T, U>builder()
                .source((Source<T>) metaInfo.getSourceInstanceFactory().newInstance())
                .interpreter((Interpreter<T, U>) metaInfo.getInterpreterInstanceFactory().newInstance())
                .sink((Sink<U>) metaInfo.getSinkInstanceFactory().newInstance())
                .batchSize(metaInfo.getBatchSize())
                .build();
    }
}

package com.livetheoogway.teflon.framework.factory;

import com.livetheoogway.teflon.framework.TaskExecutor;
import com.livetheoogway.teflon.framework.core.Interpreter;
import com.livetheoogway.teflon.framework.core.Sink;
import com.livetheoogway.teflon.framework.core.Source;
import com.livetheoogway.teflon.framework.core.meta.MetaInfo;
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

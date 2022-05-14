/*
 * Copyright 2022. Live the Oogway, Tushar Naik
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */

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

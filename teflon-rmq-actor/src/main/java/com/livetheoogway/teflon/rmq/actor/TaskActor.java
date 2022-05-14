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

package com.livetheoogway.teflon.rmq.actor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.livetheoogway.teflon.framework.StatusCallback;
import com.livetheoogway.teflon.framework.TaskScheduler;
import com.livetheoogway.teflon.framework.core.Task;
import io.appform.dropwizard.actors.ConnectionRegistry;
import io.appform.dropwizard.actors.actor.Actor;
import io.appform.dropwizard.actors.exceptionhandler.ExceptionHandlingFactory;
import io.appform.dropwizard.actors.retry.RetryStrategyFactory;
import lombok.Builder;

import java.util.Collections;

/**
 * @author tushar.naik
 * @version 1.0  19/09/17 - 10:18 PM
 */
public class TaskActor<T extends Enum<T>, M extends Task> extends Actor<T, M> {

    private final TaskScheduler taskScheduler;

    @Builder
    public TaskActor(T t, TaskScheduler taskScheduler, TeflonConfig teflonConfig,
                     ConnectionRegistry connectionRegistry, ObjectMapper mapper,
                     Class<? extends M> clazz) {
        super(t, teflonConfig.getActorConfig(), connectionRegistry, mapper, new RetryStrategyFactory(),
              new ExceptionHandlingFactory(), clazz, Collections.emptySet());
        this.taskScheduler = taskScheduler;
    }

    /**
     * Override this, to provide a custom status callback consumer
     *
     * @return Some instance of {@link StatusCallback}
     */
    public StatusCallback statusCallback() {
        return new StatusCallback() {
        };
    }

    @Override
    protected final boolean handle(M message) throws Exception {
        return taskScheduler.trigger(message, statusCallback());
    }
}

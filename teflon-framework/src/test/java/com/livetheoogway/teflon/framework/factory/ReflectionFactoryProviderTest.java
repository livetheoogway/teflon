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

import com.livetheoogway.teflon.framework.StatusCallback;
import com.livetheoogway.teflon.framework.TaskScheduler;
import com.livetheoogway.teflon.framework.core.Task;
import com.livetheoogway.teflon.framework.core.meta.TaskStat;
import com.livetheoogway.teflon.framework.impl.ConsoleSink;
import com.livetheoogway.teflon.framework.declaration.TaskActorDeclaration;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tushar.naik
 * @version 1.0  19/09/17 - 9:22 PM
 */
class ReflectionFactoryProviderTest {
    @Test
    void testDefaultConstructor() {
        TaskScheduler taskScheduler = TaskScheduler.builder()
                .declaration(TaskActorDeclaration.builder().name("number-generator")
                                     .source(NumberStreamGenerator.class)
                                     .interpreter(NumberStreamGenerator.class)
                                     .sink(ConsoleSink.class)
                                     .build())
                .build();
        AtomicReference<TaskStat<NumberStreamGenerator.NSProgress>> taskStat = new AtomicReference<>();
        assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 5), new StatusCallback<NumberStreamGenerator.NSProgress>() {
            @Override
            public void statusCallback(Task task, TaskStat<NumberStreamGenerator.NSProgress> taskStats) {
                taskStat.set(taskStats);
            }
        }));
        assertEquals(5, taskStat.get().getCountTotal());
        assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 3), new StatusCallback<NumberStreamGenerator.NSProgress>() {
            @Override
            public void statusCallback(Task task, TaskStat<NumberStreamGenerator.NSProgress> taskStats) {
                taskStat.set(taskStats);
            }
        }));
        assertEquals(3, taskStat.get().getCountTotal());
    }
}
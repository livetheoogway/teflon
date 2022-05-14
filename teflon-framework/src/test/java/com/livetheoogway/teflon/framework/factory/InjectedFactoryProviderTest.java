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
import com.livetheoogway.teflon.framework.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author tushar.naik
 * @version 1.0  18/09/17 - 12:03 PM
 */
class InjectedFactoryProviderTest {

    @Test
    void testInjection() {
        TaskScheduler taskScheduler = TestUtil.getScheduler();

        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 10), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat taskStats) {
                taskStat.set(taskStats);
            }
        }));
        assertEquals(taskStat.get().getCountTotal(), 10);
        assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 10), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat taskStats) {
                taskStat.set(taskStats);
            }
        }));
        assertEquals(taskStat.get().getCountTotal(), 10);
    }
}
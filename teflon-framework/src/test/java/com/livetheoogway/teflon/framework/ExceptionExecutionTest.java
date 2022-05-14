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

package com.livetheoogway.teflon.framework;

import com.livetheoogway.teflon.framework.core.Task;
import com.livetheoogway.teflon.framework.core.meta.TaskStat;
import com.livetheoogway.teflon.framework.factory.NumberGeneratorTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tushar.naik
 * @version 1.0  02/10/17 - 9:53 PM
 */
class ExceptionExecutionTest {
    private TaskScheduler taskScheduler;

    @BeforeEach
    public void setUp() {
        taskScheduler = TestUtil.getScheduler();
    }

    @Test
    void testCancellation() {
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        assertFalse(taskScheduler.trigger(new NumberGeneratorTask(1, 10), new StatusCallback() {
            int i = 1;

            @Override
            public void statusCallback(Task task, TaskStat taskStats) {
                taskStat.set(taskStats);
            }

            @Override
            public boolean isCancelled(Task task, TaskStat taskStat) {
                return i++ >= 5;
            }
        }));
        System.out.println("taskStat = " + taskStat);
        assertEquals(taskStat.get().getCountTotal(), 5);
    }

    @Test
    public void testException() {
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        AtomicBoolean exceptionCalled = new AtomicBoolean(false);
        assertFalse(taskScheduler.trigger(new NumberGeneratorTask(1, 10), new StatusCallback() {
            int i = 1;

            @Override
            public void statusCallback(Task task, TaskStat taskStats) {
                taskStat.set(taskStats);
            }

            @Override
            public boolean isCancelled(Task task, TaskStat taskStat) {
                if (i++ >= 5) {
                    throw new RuntimeException("");
                }
                return false;
            }

            @Override
            public void onError(Task task, TaskStat taskStat, Throwable e) {
                assertTrue(e instanceof RuntimeException);
                exceptionCalled.set(true);
            }
        }));
        assertEquals(taskStat.get().getCountTotal(), 5);
        assertTrue(exceptionCalled.get());
    }
}

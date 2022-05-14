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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tushar.naik
 * @version 1.0  02/10/17 - 2:21 AM
 */
class ScheduledExecutionTest {

    private static TaskScheduler taskScheduler;

    @BeforeAll
    static void setUp() {
        taskScheduler = TestUtil.getScheduler();
    }

    @Test
    void testScheduledExecution() throws Exception {
        AtomicReference<TaskStat<Void>> taskStat = new AtomicReference<>();
        Random random = new Random();

        taskScheduler.scheduleAtFixedRate(() -> new NumberGeneratorTask(1, random.nextInt(10) + 2),
                                          new StatusCallback() {
                                              @Override
                                              public void statusCallback(Task task, TaskStat t) {
                                                  taskStat.set(t);
                                              }
                                          }, 0, 1000, TimeUnit.MILLISECONDS);

        Thread.sleep(3000);
        assertTrue(taskStat.get().getCountTotal() > 1);
    }

    @Test
    void testScheduledFixedExecution() throws Exception {
        AtomicReference<TaskStat<Void>> taskStat = new AtomicReference<>();
        Random random = new Random();

        taskScheduler.scheduleWithFixedDelay(() -> new NumberGeneratorTask(1, random.nextInt(10) + 2),
                                             new StatusCallback() {
                                                 @Override
                                                 public void statusCallback(Task task, TaskStat t) {
                                                     taskStat.set(t);
                                                 }
                                             }, 20, 1000, TimeUnit.MILLISECONDS);

        Thread.sleep(3000);
        assertTrue(taskStat.get().getCountTotal() > 1);
    }

    @Test
    void testScheduleExecution() throws Exception {
        AtomicReference<TaskStat<Void>> taskStat = new AtomicReference<>();
        Random random = new Random();

        taskScheduler.schedule(() -> new NumberGeneratorTask(1, random.nextInt(10) + 2), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                taskStat.set(t);
            }
        }, 100, TimeUnit.MILLISECONDS);
        Thread.sleep(20);
        assertTrue(taskStat.get() == null);

        Thread.sleep(1200);
        assertTrue(taskStat.get().getCountTotal() > 1);
    }

    @Test
    void testSubmit() throws Exception {
        AtomicReference<TaskStat<Void>> taskStat = new AtomicReference<>();
        Random random = new Random();
        taskScheduler.submit(new NumberGeneratorTask(1, random.nextInt(10) + 2), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                taskStat.set(t);
            }
        });
        assertTrue(taskStat.get() == null);

        Thread.sleep(1000);
        assertTrue(taskStat.get().getCountTotal() > 1);

    }

    @Test
    void testSubmitWithResume() {
        AtomicReference<TaskStat<Void>> taskStat = new AtomicReference<>();
        Random random = new Random();

        taskScheduler.trigger(new NumberGeneratorTask(1, random.nextInt(10) + 2), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                taskStat.set(t);
            }
        });
        long countPrevious = taskStat.get().getCountTotal();
        System.out.println("countPrevious = " + countPrevious);
        taskScheduler.resume(new NumberGeneratorTask(1, random.nextInt(10) + 4), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                taskStat.set(t);
            }
        }, taskStat.get());
        System.out.println("countNow = " + taskStat.get().getCountTotal());
        assertTrue(countPrevious < taskStat.get().getCountOutputSunk());
    }
}

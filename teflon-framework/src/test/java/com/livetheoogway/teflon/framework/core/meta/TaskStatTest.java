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

package com.livetheoogway.teflon.framework.core.meta;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author tushar.naik
 * @version 1.0  28/09/17 - 3:28 PM
 */
class TaskStatTest {

    @Test
    void testStat() throws Exception {
        TaskStat taskStat = new TaskStat();
        taskStat.start();
        Thread.sleep(100);
        taskStat.setCountTotal(12);
        taskStat.end();
    }

    @Test
    void testStatException() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            TaskStat taskStat = new TaskStat();
            taskStat.start();
            taskStat.setCountTotal(12);
            taskStat.end();
            taskStat.end();
        });
    }
}
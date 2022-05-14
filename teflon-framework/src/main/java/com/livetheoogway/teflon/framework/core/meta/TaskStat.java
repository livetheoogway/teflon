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

import com.livetheoogway.teflon.framework.error.ErrorCode;
import com.livetheoogway.teflon.framework.error.TeflonError;
import lombok.*;

/**
 * A class to keep track of stats of the task
 *
 * @param <P> Progress
 * @author tushar.naik
 * @version 1.0  14/08/17 - 6:09 PM
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TaskStat<P> {
    private long countTotal;
    private long countOutputSunk;
    private long startTime;
    private long endTime;
    private String totalTime;
    private P taskProgress;

    public TaskStat(long countTotal, long countOutputSunk) {
        this.countTotal = countTotal;
        this.countOutputSunk = countOutputSunk;
        start();
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.endTime = -1;
        this.totalTime = "";
    }

    public void resume() {
        this.endTime = -1;
        this.totalTime = "";
    }

    public void end() {
        if (endTime > 0) {
            throw new TeflonError(ErrorCode.TASK_ALREADY_ENDED, "End has already been called once");
        }
        this.endTime = System.currentTimeMillis();
        this.totalTime = endTime - startTime + "ms";
    }

    /**
     * @return elapsed time, if end is not yet called for the stat
     */
    public String getElapsedTime() {
        if (endTime < 0) {
            return System.currentTimeMillis() - startTime + "ms";
        }
        return totalTime;
    }
}

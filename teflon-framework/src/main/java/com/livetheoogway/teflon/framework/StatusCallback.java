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

import com.livetheoogway.teflon.framework.core.meta.TaskStat;
import com.livetheoogway.teflon.framework.core.Task;

/**
 * This interface houses a bunch of Callbacks, called during the execution lifecycle of a {@link Task}
 * These are more like consumers, all callbacks are called by {@link TaskExecutor}
 * All are defaulted with empty functions.
 * Override the callbacks you wish to get
 *
 * @author tushar.naik
 * @version 1.0  02/10/17 - 8:30 PM
 */
public interface StatusCallback<P> {

    /**
     * callback during init of the task
     *
     * @param task     task
     * @param taskStat stats of the task
     */
    default void onInit(final Task task, final TaskStat<P> taskStat) {
    }

    /**
     * callback during resumption of the task
     *
     * @param task     task
     * @param taskStat stats of the task
     */
    default void onResume(final Task task, final TaskStat<P> taskStat) {
    }

    /**
     * callback at regular intervals, when a batchSize is reached
     *
     * @param task     task
     * @param taskStat stats of the task
     */
    default void statusCallback(final Task task, final TaskStat<P> taskStat) {
    }

    /**
     * called at regular intervals, when a batchSize is reached
     *
     * @param task     task
     * @param taskStat stats of the task
     * @return true if you wish to cancel the execution of the task
     */
    default boolean isCancelled(final Task task, final TaskStat<P> taskStat) {
        return false;
    }

    /**
     * callback after completion of task
     *
     * @param task     task
     * @param taskStat stats of the task
     */
    default void onComplete(final Task task, final TaskStat<P> taskStat) {
    }

    /**
     * callback when an exception is thrown
     *
     * @param task     task
     * @param taskStat stats of the task
     * @param e        error that was run into
     */
    default void onError(final Task task, final TaskStat<P> taskStat, final Throwable e) {
    }
}

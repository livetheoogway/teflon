package com.teflon.task.framework;

import com.teflon.task.framework.core.Task;
import com.teflon.task.framework.core.meta.TaskStat;

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
     */
    default void onInit(Task task, TaskStat<P> taskStat) {
    }

    /**
     * callback during init of the task
     */
    default void onResume(Task task, TaskStat<P> taskStat) {
    }

    /**
     * callback at regular intervals, when a batchSize is reached
     */
    default void statusCallback(Task task, TaskStat<P> taskStat) {
    }

    /**
     * called at regular intervals, when a batchSize is reached
     *
     * @return true if you wish to cancel the execution of the task
     */
    default boolean isCancelled(Task task, TaskStat<P> taskStat) {
        return false;
    }

    /**
     * callback after completion of task
     */
    default void onComplete(Task task, TaskStat<P> taskStat) {
    }

    /**
     * callback when an exception is thrown
     */
    default void onError(Task task, TaskStat taskStat, Throwable e) {
    }
}

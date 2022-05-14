package com.livetheoogway.teflon.framework.core;

import com.livetheoogway.teflon.framework.core.meta.TaskStat;

/**
 * @author tushar.naik
 * @version 1.0  30/05/18 - 10:14 AM
 */
public interface NonResumableSource<Input> extends Source<Input, Void> {

    default void resume(Task task, TaskStat taskStat) {
        throw new UnsupportedOperationException("Task " + task.name() + "cannot be resumed");
    }
}

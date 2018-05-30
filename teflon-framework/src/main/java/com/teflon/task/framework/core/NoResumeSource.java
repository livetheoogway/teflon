package com.teflon.task.framework.core;

import com.teflon.task.framework.core.meta.TaskStat;

/**
 * @author tushar.naik
 * @version 1.0  30/05/18 - 10:14 AM
 */
public interface NoResumeSource<Input, Progress> extends Source<Input, Progress> {

    default void resume(Task init, TaskStat taskStat) {
        throw new UnsupportedOperationException("Task " + init.name() + "cannot be resumed");
    }
}

package com.teflon.task.framework.core;

import com.teflon.task.framework.core.meta.TaskStat;

/**
 * @author tushar.naik
 * @version 1.0  10/08/17 - 6:05 PM
 */
public interface Source<Input, Progress> extends Unit {

    /**
     * resume a task
     */
    void resume(Task init, TaskStat<Progress> taskStat) throws Exception;


    /**
     * get a single InterimOutput from Source
     * returns null if stream is empty
     *
     * @return Input
     * @throws Exception while getting input
     */
    SourceInputs<Input, Progress> getInput() throws Exception;
}

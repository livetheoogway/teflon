package com.livetheoogway.teflon.framework.core;

import com.livetheoogway.teflon.framework.core.meta.TaskStat;

/**
 * @author tushar.naik
 * @version 1.0  10/08/17 - 6:05 PM
 */
public interface Source<Input, Progress> extends Unit {

    /**
     * resume a task
     *
     * @param task     task
     * @param taskStat stats of the task
     * @throws Exception if something wrong happens on resume
     */
    void resume(Task task, TaskStat<Progress> taskStat) throws Exception;


    /**
     * get a single InterimOutput from Source
     * returns null if stream is empty
     *
     * @return Input
     * @throws Exception while getting input
     */
    SourceInputs<Input, Progress> getInput() throws Exception;
}

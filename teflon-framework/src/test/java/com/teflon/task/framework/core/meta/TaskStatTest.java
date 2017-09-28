package com.teflon.task.framework.core.meta;

import org.junit.Test;

/**
 * @author tushar.naik
 * @version 1.0  28/09/17 - 3:28 PM
 */
public class TaskStatTest {

    @Test
    public void testStat() throws Exception {
        TaskStat taskStat = new TaskStat();
        taskStat.start();
        Thread.sleep(100);
        taskStat.setCountTotal(12);
        taskStat.end();
    }

    @Test(expected = RuntimeException.class)
    public void testStatException() throws Exception {
        TaskStat taskStat = new TaskStat();
        taskStat.start();
        taskStat.setCountTotal(12);
        taskStat.end();
        taskStat.end();
    }
}
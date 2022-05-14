package com.livetheoogway.teflon.framework.core.meta;

import com.livetheoogway.teflon.framework.error.TeflonError;
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
        Assertions.assertThrows(TeflonError.class, () -> {
            TaskStat taskStat = new TaskStat();
            taskStat.start();
            taskStat.setCountTotal(12);
            taskStat.end();
            taskStat.end();
        });
    }
}
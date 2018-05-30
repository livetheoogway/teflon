package com.teflon.task.framework;

import com.teflon.task.framework.core.Task;
import com.teflon.task.framework.core.meta.TaskStat;
import com.teflon.task.framework.factory.NumberGeneratorTask;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tushar.naik
 * @version 1.0  02/10/17 - 9:53 PM
 */
public class ExceptionExecutionTest {
    private TaskScheduler taskScheduler;

    @Before
    public void setUp() {
        taskScheduler = TestUtil.getScheduler();
    }

    @Test
    public void testCancellation() throws Exception {
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        Assert.assertFalse(taskScheduler.trigger(new NumberGeneratorTask(1, 10), new StatusCallback() {
            int i = 1;

            @Override
            public void statusCallback(Task task, TaskStat taskStats) {
                taskStat.set(taskStats);
            }

            @Override
            public boolean isCancelled(Task task, TaskStat taskStat) {
                return i++ >= 5;
            }
        }));
        System.out.println("taskStat = " + taskStat);
        Assert.assertEquals(taskStat.get().getCountTotal(), 5);
    }

    @Test
    public void testException() throws Exception {
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        AtomicBoolean exceptionCalled = new AtomicBoolean(false);
        Assert.assertFalse(taskScheduler.trigger(new NumberGeneratorTask(1, 10), new StatusCallback() {
            int i = 1;

            @Override
            public void statusCallback(Task task, TaskStat taskStats) {
                taskStat.set(taskStats);
            }

            @Override
            public boolean isCancelled(Task task, TaskStat taskStat) {
                if (i++ >= 5) {
                    throw new RuntimeException("");
                }
                return false;
            }

            @Override
            public void onError(Task task, TaskStat taskStat, Throwable e) {
                Assert.assertTrue(e instanceof RuntimeException);
                exceptionCalled.set(true);
            }
        }));
        Assert.assertEquals(taskStat.get().getCountTotal(), 5);
        Assert.assertTrue(exceptionCalled.get());
    }
}

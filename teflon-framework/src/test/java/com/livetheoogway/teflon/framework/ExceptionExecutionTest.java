package com.livetheoogway.teflon.framework;

import com.livetheoogway.teflon.framework.core.Task;
import com.livetheoogway.teflon.framework.core.meta.TaskStat;
import com.livetheoogway.teflon.framework.factory.NumberGeneratorTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tushar.naik
 * @version 1.0  02/10/17 - 9:53 PM
 */
public class ExceptionExecutionTest {
    private TaskScheduler taskScheduler;

    @BeforeEach
    public void setUp() {
        taskScheduler = TestUtil.getScheduler();
    }

    @Test
    public void testCancellation() {
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        assertFalse(taskScheduler.trigger(new NumberGeneratorTask(1, 10), new StatusCallback() {
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
        assertEquals(taskStat.get().getCountTotal(), 5);
    }

    @Test
    public void testException() {
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        AtomicBoolean exceptionCalled = new AtomicBoolean(false);
        assertFalse(taskScheduler.trigger(new NumberGeneratorTask(1, 10), new StatusCallback() {
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
                assertTrue(e instanceof RuntimeException);
                exceptionCalled.set(true);
            }
        }));
        assertEquals(taskStat.get().getCountTotal(), 5);
        assertTrue(exceptionCalled.get());
    }
}

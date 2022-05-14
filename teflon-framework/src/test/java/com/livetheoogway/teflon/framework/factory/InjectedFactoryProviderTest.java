package com.livetheoogway.teflon.framework.factory;

import com.livetheoogway.teflon.framework.StatusCallback;
import com.livetheoogway.teflon.framework.TaskScheduler;
import com.livetheoogway.teflon.framework.core.Task;
import com.livetheoogway.teflon.framework.core.meta.TaskStat;
import com.livetheoogway.teflon.framework.TestUtil;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
/**
 * @author tushar.naik
 * @version 1.0  18/09/17 - 12:03 PM
 */
public class InjectedFactoryProviderTest {

    @Test
    public void testInjection() throws Exception {
        TaskScheduler taskScheduler = TestUtil.getScheduler();

        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 10), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat taskStats) {
                taskStat.set(taskStats);
            }
        }));
        assertEquals(taskStat.get().getCountTotal(), 10);
        assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 10), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat taskStats) {
                taskStat.set(taskStats);
            }
        }));
        assertEquals(taskStat.get().getCountTotal(), 10);
    }
}
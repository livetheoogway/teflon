package com.teflon.task.framework.factory;

import com.teflon.task.framework.StatusCallback;
import com.teflon.task.framework.TaskScheduler;
import com.teflon.task.framework.core.Task;
import com.teflon.task.framework.core.meta.TaskStat;
import com.teflon.task.framework.declaration.TaskActorDeclaration;
import com.teflon.task.framework.impl.ConsoleSink;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tushar.naik
 * @version 1.0  19/09/17 - 9:22 PM
 */
public class ReflectionFactoryProviderTest {
    @Test
    public void testDefaultConstructor() throws Exception {
        TaskScheduler taskScheduler = TaskScheduler.builder()
                                                   .declaration(TaskActorDeclaration.builder().name("number-generator")
                                                                                    .source(NumberStreamGenerator.class)
                                                                                    .interpreter(NumberStreamGenerator.class)
                                                                                    .sink(ConsoleSink.class)
                                                                                    .build())
                                                   .build();
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        Assert.assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 5), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat taskStats) {
                taskStat.set(taskStats);
            }
        }));
        Assert.assertEquals(taskStat.get().getCountTotal(), 5);
        Assert.assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 3), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat taskStats) {
                taskStat.set(taskStats);
            }
        }));
        Assert.assertEquals(taskStat.get().getCountTotal(), 3);
    }
}
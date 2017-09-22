package com.teflon.task.framework;

import com.teflon.task.framework.core.meta.TaskStat;
import com.teflon.task.framework.declaration.TaskActorDeclaration;
import com.teflon.task.framework.error.TeflonError;
import com.teflon.task.framework.factory.NumberGeneratorTask;
import com.teflon.task.framework.factory.NumberStreamGenerator;
import com.teflon.task.framework.impl.ConsoleSink;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tushar.naik
 * @version 1.0  18/09/17 - 11:54 AM
 */
public class TaskSchedulerExceptionScenariosTest {

    @Test(expected = TeflonError.class)
    public void testNullSource() throws Exception {
        TaskScheduler.builder().declaration(TaskActorDeclaration.builder().name("number-generator").build()).build();
    }


    @Test(expected = TeflonError.class)
    public void testNullInterpreter() throws Exception {
        TaskScheduler.builder().declaration(TaskActorDeclaration.builder().name("number-generator")
                                                                .source(NumberStreamGenerator.class)
                                                                .build()).build();
    }

    @Test(expected = TeflonError.class)
    public void testAbstractModuleAbsent() throws Exception {
        TaskScheduler.builder().classPath("com.teflon.task.framework.factory").build();
    }

    @Test
    public void testDefaultConstructorError() throws Exception {
        TaskScheduler taskScheduler = TaskScheduler.builder()
                                                   .declaration(TaskActorDeclaration.builder().name("number-generator")
                                                                                    .source(NumberStreamGenerator.class)
                                                                                    .interpreter(NumberStreamGenerator.class)
                                                                                    .sink(ConsoleSink.class)
                                                                                    .build())
                                                   .build();
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        Assert.assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 5), taskStat::set));
        Assert.assertEquals(taskStat.get().getCountTotal(), 5);
        Assert.assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 3), taskStat::set));
        Assert.assertEquals(taskStat.get().getCountTotal(), 3);
    }
}
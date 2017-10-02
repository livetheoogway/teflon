package com.teflon.task.framework;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.teflon.task.framework.core.Task;
import com.teflon.task.framework.core.meta.TaskStat;
import com.teflon.task.framework.factory.NumberGeneratorTask;
import com.teflon.task.framework.factory.NumberStreamGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tushar.naik
 * @version 1.0  02/10/17 - 2:21 AM
 */
public class ScheduledExecutionTest {

    @Test
    public void testScheduledExecution() throws Exception {
        TaskScheduler taskScheduler
                = TaskScheduler.builder()
                               .classPath("com.teflon.task.framework.factory")
                               .injectorProvider(() -> Guice.createInjector(new AbstractModule() {
                                   @Override
                                   protected void configure() {
                                   }

                                   @Provides
                                   public NumberStreamGenerator getSimpleImpl() {
                                       return new NumberStreamGenerator();
                                   }
                               }))
                               .poolSize(10)
                               .build();
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        Random random = new Random();
        random.nextInt(10);

        taskScheduler.scheduleAtFixedRate(() -> new NumberGeneratorTask(1, random.nextInt(10) + 1), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                if (taskStat.get() == null) {
                    taskStat.set(t);
                } else {
                    taskStat.get().add(t);
                }
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);

        Thread.sleep(5000);
        Assert.assertTrue(taskStat.get().getCountTotal() > 1);
    }

    @Test
    public void testScheduledFixedExecution() throws Exception {
        TaskScheduler taskScheduler
                = TaskScheduler.builder()
                               .classPath("com.teflon.task.framework.factory")
                               .injectorProvider(() -> Guice.createInjector(new AbstractModule() {
                                   @Override
                                   protected void configure() {
                                   }

                                   @Provides
                                   public NumberStreamGenerator getSimpleImpl() {
                                       return new NumberStreamGenerator();
                                   }
                               }))
                               .poolSize(10)
                               .build();
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        Random random = new Random();
        random.nextInt(10);

        taskScheduler.scheduleWithFixedDelay(() -> new NumberGeneratorTask(1, random.nextInt(10) + 1), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                if (taskStat.get() == null) {
                    taskStat.set(t);
                } else {
                    taskStat.get().add(t);
                }
            }
        }, 20, 1000, TimeUnit.MILLISECONDS);

        Thread.sleep(5000);
        Assert.assertTrue(taskStat.get().getCountTotal() > 1);
    }

    @Test
    public void testScheduleExecution() throws Exception {
        TaskScheduler taskScheduler
                = TaskScheduler.builder()
                               .classPath("com.teflon.task.framework.factory")
                               .injectorProvider(() -> Guice.createInjector(new AbstractModule() {
                                   @Override
                                   protected void configure() {
                                   }

                                   @Provides
                                   public NumberStreamGenerator getSimpleImpl() {
                                       return new NumberStreamGenerator();
                                   }
                               }))
                               .poolSize(10)
                               .build();
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        Random random = new Random();
        random.nextInt(10);

        taskScheduler.schedule(() -> new NumberGeneratorTask(1, random.nextInt(10) + 1), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                if (taskStat.get() == null) {
                    taskStat.set(t);
                } else {
                    taskStat.get().add(t);
                }
            }
        }, 100, TimeUnit.MILLISECONDS);
        Thread.sleep(20);
        Assert.assertTrue(taskStat.get() == null);

        Thread.sleep(500);
        Assert.assertTrue(taskStat.get().getCountTotal() > 1);

    }

    @Test
    public void testSubmit() throws Exception {
        TaskScheduler taskScheduler
                = TaskScheduler.builder()
                               .classPath("com.teflon.task.framework.factory")
                               .injectorProvider(() -> Guice.createInjector(new AbstractModule() {
                                   @Override
                                   protected void configure() {
                                   }

                                   @Provides
                                   public NumberStreamGenerator getSimpleImpl() {
                                       return new NumberStreamGenerator();
                                   }
                               }))
                               .poolSize(10)
                               .build();
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        Random random = new Random();
        random.nextInt(10);

        taskScheduler.submit(new NumberGeneratorTask(1, random.nextInt(10) + 1), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                if (taskStat.get() == null) {
                    taskStat.set(t);
                } else {
                    taskStat.get().add(t);
                }
            }
        });
        Assert.assertTrue(taskStat.get() == null);

        Thread.sleep(300);
        Assert.assertTrue(taskStat.get().getCountTotal() > 1);

    }
}

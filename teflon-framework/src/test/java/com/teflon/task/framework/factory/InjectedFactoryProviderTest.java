package com.teflon.task.framework.factory;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.teflon.task.framework.TaskScheduler;
import com.teflon.task.framework.core.meta.TaskStat;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tushar.naik
 * @version 1.0  18/09/17 - 12:03 PM
 */
public class InjectedFactoryProviderTest {

    @Test
    public void testInjection() throws Exception {
        TaskScheduler taskScheduler
                = TaskScheduler.builder()
                               .classPath("com.teflon.task.framework.factory")
                               .abstractModule(new AbstractModule() {
                                   @Override
                                   protected void configure() {
                                   }

                                   @Provides
                                   public NumberStreamGenerator getSimpleImpl() {
                                       return new NumberStreamGenerator();
                                   }
                               })
                               .build();
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        Assert.assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 10), taskStat::set));
        Assert.assertEquals(taskStat.get().getCountTotal(), 10);
        Assert.assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 10), taskStat::set));
        Assert.assertEquals(taskStat.get().getCountTotal(), 10);
    }
}
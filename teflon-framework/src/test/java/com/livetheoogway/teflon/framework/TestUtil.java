package com.livetheoogway.teflon.framework;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.livetheoogway.teflon.framework.factory.NumberStreamGenerator;

/**
 * @author tushar.naik
 * @version 1.0  30/05/18 - 11:37 AM
 */
public interface TestUtil {

    static TaskScheduler getScheduler() {
        return TaskScheduler.builder()
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
    }
}

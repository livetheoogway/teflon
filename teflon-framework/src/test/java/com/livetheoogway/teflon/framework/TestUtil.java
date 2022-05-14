/*
 * Copyright 2022. Live the Oogway, Tushar Naik
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */

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
                            .classPath("com.livetheoogway.teflon.framework.factory")
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

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

package com.livetheoogway.teflon.framework.declaration.annotated;

import com.livetheoogway.teflon.framework.core.Interpreter;
import com.livetheoogway.teflon.framework.core.Sink;
import com.livetheoogway.teflon.framework.core.Source;
import com.livetheoogway.teflon.framework.factory.FactoryType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 6:14 PM
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskDeclaration {
    /**
     * @return Type of {@link Source}
     */
    Class<? extends Source<?, ?>> source();

    /**
     * @return Type of {@link Interpreter}
     */
    Class<? extends Interpreter<?, ?>> interpreter();

    /**
     * @return Type of {@link Sink}
     */
    Class<? extends Sink<?>> sink();

    /**
     * @return Name of Task Declaration
     */
    String name();

    /**
     * @return Type of object creation factory for the Actors
     */
    FactoryType factoryType();

    /**
     * batchSize when updates are called
     *
     * @return size
     */
    int batchSize() default 1;
}

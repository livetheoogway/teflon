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

package com.livetheoogway.teflon.framework.declaration;

import com.livetheoogway.teflon.framework.core.Interpreter;
import com.livetheoogway.teflon.framework.core.Sink;
import com.livetheoogway.teflon.framework.core.Source;
import com.livetheoogway.teflon.framework.factory.FactoryType;
import lombok.Builder;
import lombok.Getter;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 2:08 AM
 */
@Getter
@Builder
public class TaskActorDeclaration {

    /* associate the declaration to a name */
    private String name;

    /* source class for task */
    private Class<? extends Source<?, ?>> source;

    /* interpreter class for task */
    private Class<? extends Interpreter<?, ?>> interpreter;

    /* sink class for task */
    private Class<? extends Sink<?>> sink;

    /* type of object providers (factory) */
    @Builder.Default
    private FactoryType factoryType = FactoryType.DEFAULT_CONSTRUCTOR_REFLECTION;

    /* batch size for the job */
    @Builder.Default
    private int batchSize = 1;
}

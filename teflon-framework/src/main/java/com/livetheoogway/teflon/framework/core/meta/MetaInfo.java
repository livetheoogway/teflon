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

package com.livetheoogway.teflon.framework.core.meta;

import com.livetheoogway.teflon.framework.core.Sink;
import com.livetheoogway.teflon.framework.core.Source;
import com.livetheoogway.teflon.framework.core.Interpreter;
import com.livetheoogway.teflon.framework.factory.InstanceFactory;
import lombok.Builder;
import lombok.Getter;

/**
 * A meta class that houses all required instance generating factories
 * and other meta properties
 *
 * @author tushar.naik
 * @version 1.0  16/09/17 - 1:47 AM
 */
@Builder
@Getter
public class MetaInfo {
    /* source object provider */
    private final InstanceFactory<? extends Source<?, ?>> sourceInstanceFactory;

    /* interpreter object provider */
    private final InstanceFactory<? extends Interpreter<?, ?>> interpreterInstanceFactory;

    /* sink object provider */
    private final InstanceFactory<? extends Sink<?>> sinkInstanceFactory;

    /* batch size for task execution */
    private final int batchSize;

    @Override
    public String toString() {
        return "MetaInfo[" + "source:" + sourceInstanceFactory +
                ", interpreter:" + interpreterInstanceFactory +
                ", sink:" + sinkInstanceFactory +
                ", batchSize:" + batchSize +
                ']';
    }
}

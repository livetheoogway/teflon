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

package com.livetheoogway.teflon.framework.core;

import java.util.List;

/**
 * A sink to all outputs
 *
 * @param <O> Output
 * @author tushar.naik
 * @version 1.0  10/08/17 - 6:05 PM
 */
public interface Sink<O> extends Unit {

    /**
     * sink a bunch of outputs
     *
     * @param outputs outputs after interpretation
     */
    void sink(List<O> outputs);

}

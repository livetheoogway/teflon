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

package com.livetheoogway.teflon.framework.impl;

import com.livetheoogway.teflon.framework.core.Sink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  17/09/17 - 11:59 PM
 */
@Slf4j
public class LogSink<T> implements Sink<T> {

    @Override
    public void sink(List<T> ts) {
        log.info(ts.toString());
    }
}

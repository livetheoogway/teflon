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
import com.livetheoogway.teflon.framework.declaration.annotated.SinkDeclaration;

import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 7:53 PM
 */
@SinkDeclaration(takes = String.class)
public class ConsoleSink implements Sink<String> {

    @Override
    public void sink(List<String> items) {
        items.forEach(System.out::println);
    }
}

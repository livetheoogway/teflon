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

package com.livetheoogway.teflon.framework.container;

import com.google.common.collect.Maps;
import com.livetheoogway.teflon.framework.error.ErrorCode;
import com.livetheoogway.teflon.framework.error.TeflonError;

import java.util.Map;
import java.util.Set;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 1:46 AM
 */
public class MapContainer<T> implements IContainer<String, T> {

    private final Map<String, T> taskMappings = Maps.newHashMap();

    @Override
    public void register(final String name, final T metaInfo) {
        if (taskMappings.containsKey(name)) {
            throw new TeflonError(ErrorCode.MULTIPLE_MAPPING_ERROR, "Mapping already exists with the name:" + name);
        }
        taskMappings.put(name, metaInfo);
    }

    @Override
    public T get(final String name) {
        return taskMappings.get(name);
    }

    @Override
    public Set<String> keys() {
        return taskMappings.keySet();
    }
}

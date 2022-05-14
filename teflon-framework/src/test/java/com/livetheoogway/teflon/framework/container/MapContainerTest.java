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

import com.google.common.collect.Sets;
import com.livetheoogway.teflon.framework.error.TeflonError;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tushar.naik
 * @version 1.0  28/09/17 - 3:24 PM
 */
class MapContainerTest {

    @Test
    void testContainer() {
        MapContainer<Integer> mapContainer = new MapContainer<>();
        mapContainer.register("key1", 123);
        mapContainer.register("key2", 234);
        mapContainer.register("key3", 345);
        mapContainer.register("key4", 456);

        assertEquals(mapContainer.get("key3").intValue(), 345);
        assertEquals(mapContainer.keys(), Sets.newHashSet("key1", "key2", "key3", "key4"));
    }

    @Test
    void testContainerException() {
        assertThrows(TeflonError.class, () -> {
            MapContainer<Integer> mapContainer = new MapContainer<>();
            mapContainer.register("key1", 123);
            mapContainer.register("key1", 234);
        });
    }
}
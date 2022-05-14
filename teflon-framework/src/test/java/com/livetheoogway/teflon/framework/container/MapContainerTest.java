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
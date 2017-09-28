package com.teflon.task.framework.container;

import com.google.common.collect.Sets;
import com.teflon.task.framework.error.TeflonError;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author tushar.naik
 * @version 1.0  28/09/17 - 3:24 PM
 */
public class MapContainerTest {

    @Test
    public void testContainer() throws Exception {
        MapContainer<Integer> mapContainer = new MapContainer<>();
        mapContainer.register("key1", 123);
        mapContainer.register("key2", 234);
        mapContainer.register("key3", 345);
        mapContainer.register("key4", 456);

        Assert.assertEquals(mapContainer.get("key3").intValue(), 345);
        Assert.assertEquals(mapContainer.keys(), Sets.newHashSet("key1", "key2", "key3","key4"));
    }

    @Test(expected = TeflonError.class)
    public void testContainerException() throws Exception {
        MapContainer<Integer> mapContainer = new MapContainer<>();
        mapContainer.register("key1", 123);
        mapContainer.register("key1", 234);
    }
}
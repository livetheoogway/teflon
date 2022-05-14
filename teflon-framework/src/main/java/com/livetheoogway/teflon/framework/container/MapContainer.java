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

    private Map<String, T> taskMappings = Maps.newHashMap();

    @Override
    public void register(String name, T metaInfo) {
        if (taskMappings.containsKey(name)) {
            throw new TeflonError(ErrorCode.MULTIPLE_MAPPING_ERROR, "Mapping already exists with the name:" + name);
        }
        taskMappings.put(name, metaInfo);
    }

    @Override
    public T get(String name) {
        return taskMappings.get(name);
    }

    @Override
    public Set<String> keys(){
        return taskMappings.keySet();
    }
}

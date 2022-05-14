package com.livetheoogway.teflon.framework.factory;

import com.livetheoogway.teflon.framework.error.ErrorCode;
import com.livetheoogway.teflon.framework.error.TeflonError;

import java.lang.reflect.InvocationTargetException;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 9:30 PM
 */
public class ReflectionFactoryProvider implements FactoryProvider {

    @Override
    public <T> InstanceFactory<T> instanceFactory(Class<T> clazz) {
        return () -> {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                throw new TeflonError(ErrorCode.INSTANCE_CREATION_EXCEPTION,
                                      "Error while creating instance for class:" + clazz.getSimpleName());
            }
        };
    }
}

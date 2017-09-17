package com.teflon.task.framework.factory;

import com.teflon.task.framework.error.TeflonError;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 9:15 PM
 */
public interface FactoryProvider {
    <T> InstanceFactory<T> instanceFactory(Class<T> clazz) throws TeflonError;
}

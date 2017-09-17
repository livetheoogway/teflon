package com.teflon.task.core.factory;

import com.teflon.task.core.core.InstanceFactory;
import com.teflon.task.core.error.TeflonError;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 9:15 PM
 */
public interface FactoryProvider {
    <T> InstanceFactory<T> instanceFactory(Class<T> clazz) throws TeflonError;
}

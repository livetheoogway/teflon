package com.teflon.task.core.core;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 1:49 AM
 */
@FunctionalInterface
public interface InstanceFactory<T> {
    T newInstance();
}

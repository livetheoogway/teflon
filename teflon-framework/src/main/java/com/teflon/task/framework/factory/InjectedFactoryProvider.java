package com.teflon.task.framework.factory;

import com.google.inject.Injector;
import lombok.AllArgsConstructor;

import java.util.function.Supplier;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 9:29 PM
 */
@AllArgsConstructor
public class InjectedFactoryProvider implements FactoryProvider {
    private Supplier<Injector> injectorProvider;

    @Override
    public <T> InstanceFactory<T> instanceFactory(Class<T> clazz) {
        return () -> injectorProvider.get().getInstance(clazz);
    }
}

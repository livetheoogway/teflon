package com.teflon.task.framework.factory;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import lombok.Getter;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 9:29 PM
 */
public class InjectedFactoryProvider implements FactoryProvider {

    @Getter
    private Injector injector;

    public InjectedFactoryProvider(AbstractModule abstractModule) {
        injector = Guice.createInjector(abstractModule);
    }

    @Override
    public <T> InstanceFactory<T> instanceFactory(Class<T> clazz) {
        return () -> injector.getInstance(clazz);
    }
}

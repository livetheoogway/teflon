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

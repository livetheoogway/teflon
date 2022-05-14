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

package com.livetheoogway.teflon.framework.core;

/**
 * A set of defaults used by {@link Source},{@link Interpreter},{@link Sink}
 * This may be overridden to perform appropriate functions
 * Override this class, and cast {@link Task} to whatever instance of {@link Task} you expect.
 *
 * @author tushar.naik
 * @version 1.0  21/09/17 - 3:36 AM
 */
public interface Unit {
    /**
     * initiate the Unit for a task
     *
     * @param task for init
     */
    default void init(Task task) {
    }

    /**
     * close the Unit
     */
    default void close() {
    }

    /**
     * abort and rollback if required
     */
    default void abort() {
    }
}

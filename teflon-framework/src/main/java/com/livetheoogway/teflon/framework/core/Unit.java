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

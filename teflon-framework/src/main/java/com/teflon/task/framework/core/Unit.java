package com.teflon.task.framework.core;

import java.io.IOException;

/**
 * A set of defaults used by {@link Source},{@link Interpreter},{@link Sink}
 * This may be overridden to perform appropriate functions
 *
 * @author tushar.naik
 * @version 1.0  21/09/17 - 3:36 AM
 */
public interface Unit {
    /**
     * initiate the Unit for a task
     */
    default void init(Task init) throws Exception {
    }

    /**
     * close the Unit
     */
    default void close() throws IOException {
    }

    /**
     * abort and rollback if required
     */
    default void abort() {
    }
}

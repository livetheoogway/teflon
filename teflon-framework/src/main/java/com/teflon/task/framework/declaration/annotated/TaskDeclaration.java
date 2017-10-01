package com.teflon.task.framework.declaration.annotated;

import com.teflon.task.framework.core.Interpreter;
import com.teflon.task.framework.core.Sink;
import com.teflon.task.framework.core.Source;
import com.teflon.task.framework.factory.FactoryType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 6:14 PM
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskDeclaration {
    /**
     * Type of {@link Source}
     */
    Class<? extends Source> source();

    /**
     * Type of {@link Interpreter}
     */
    Class<? extends Interpreter> interpreter();

    /**
     * Type of {@link Sink}
     */
    Class<? extends Sink> sink();

    /**
     * Name of Task Declaration
     */
    String name();

    /**
     * Type of object creation factory for the Actors
     */
    FactoryType factoryType();

    /**
     * batchSize when updates are called
     * @return
     */
    int batchSize() default 1;
}

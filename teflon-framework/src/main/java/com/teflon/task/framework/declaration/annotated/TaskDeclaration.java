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
    Class<? extends Source> source();

    Class<? extends Interpreter> interpreter();

    Class<? extends Sink> sink();

    String name();

    FactoryType factoryType();

    int batchSize() default 1;
}

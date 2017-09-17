package com.teflon.task.core;

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
}

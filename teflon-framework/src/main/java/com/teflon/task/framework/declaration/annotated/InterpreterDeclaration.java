package com.teflon.task.framework.declaration.annotated;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 12:24 AM
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface InterpreterDeclaration {
    Class<?> takes();
    Class<?> emits();
}

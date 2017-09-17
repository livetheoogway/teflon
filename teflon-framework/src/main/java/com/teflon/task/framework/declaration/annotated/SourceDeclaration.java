package com.teflon.task.core.impl2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 12:16 AM
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SourceDeclaration {
    Class<?> emits();
}

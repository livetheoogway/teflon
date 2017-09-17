package com.teflon.task.core;

import lombok.Builder;
import lombok.Getter;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 2:08 AM
 */
@Getter
@Builder
public class TaskActorDeclaration {
    private String name;
    private Class<? extends Source> source;
    private Class<? extends Interpreter> interpreter;
    private Class<? extends Sink> sink;
    private FactoryType factoryType;
}

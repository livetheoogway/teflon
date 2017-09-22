package com.teflon.task.framework.declaration;

import com.teflon.task.framework.core.Interpreter;
import com.teflon.task.framework.core.Sink;
import com.teflon.task.framework.core.Source;
import com.teflon.task.framework.core.Task;
import com.teflon.task.framework.factory.FactoryType;
import lombok.Builder;
import lombok.Getter;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 2:08 AM
 */
@Getter
@Builder
public class TaskActorDeclaration {
    private Class<? extends Task> task;
    private Class<? extends Source> source;
    private Class<? extends Interpreter> interpreter;
    private Class<? extends Sink> sink;

    @Builder.Default
    private FactoryType factoryType = FactoryType.DEFAULT_CONSTRUCTOR_REFLECTION;

    @Builder.Default
    private int batchSize = 1;
}

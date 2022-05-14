package com.livetheoogway.teflon.framework.declaration;

import com.livetheoogway.teflon.framework.core.Interpreter;
import com.livetheoogway.teflon.framework.core.Sink;
import com.livetheoogway.teflon.framework.core.Source;
import com.livetheoogway.teflon.framework.factory.FactoryType;
import lombok.Builder;
import lombok.Getter;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 2:08 AM
 */
@Getter
@Builder
public class TaskActorDeclaration {

    /* associate the declaration to a name */
    private String name;

    /* source class for task */
    private Class<? extends Source> source;

    /* interpreter class for task */
    private Class<? extends Interpreter> interpreter;

    /* sink class for task */
    private Class<? extends Sink> sink;

    /* type of object providers (factory) */
    @Builder.Default
    private FactoryType factoryType = FactoryType.DEFAULT_CONSTRUCTOR_REFLECTION;

    /* batch size for the job */
    @Builder.Default
    private int batchSize = 1;
}

package com.teflon.task.framework;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.teflon.task.framework.container.MapContainer;
import com.teflon.task.framework.core.*;
import com.teflon.task.framework.core.meta.MetaInfo;
import com.teflon.task.framework.declaration.TaskActorDeclaration;
import com.teflon.task.framework.declaration.annotated.InterpreterDeclaration;
import com.teflon.task.framework.declaration.annotated.SinkDeclaration;
import com.teflon.task.framework.declaration.annotated.SourceDeclaration;
import com.teflon.task.framework.error.ErrorCode;
import com.teflon.task.framework.error.TeflonError;
import com.teflon.task.framework.factory.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;

import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 6:57 PM
 */
@AllArgsConstructor
public final class TaskScheduler {

    private MapContainer<MetaInfo> mContainer;

    public void trigger(Task task) throws TeflonError {
        MetaInfo metaInfo = mContainer.get(task.name());
        new ExecutionFactory<>(metaInfo).newInstance().initiate(task, null, () -> false);
    }

    @Builder
    public TaskScheduler(@Singular List<TaskActorDeclaration> declarations,
                         AbstractModule abstractModule) {
        mContainer = new MapContainer<>();
        declarations.forEach(k -> {
            verify(k);
            FactoryProvider factoryProvider = factoryProvider(k.getFactoryType(), abstractModule);
            mContainer.register(k.getName(),
                                MetaInfo.builder()
                                        .sourceInstanceFactory(factoryProvider.instanceFactory(k.getSource()))
                                        .interpreterInstanceFactory(factoryProvider.instanceFactory(k.getInterpreter()))
                                        .sinkInstanceFactory(factoryProvider.instanceFactory(k.getSink()))
                                        .build());
        });
    }

    private static void verify(TaskActorDeclaration taskActorDeclaration) {
        verify(taskActorDeclaration.getSource(),
               taskActorDeclaration.getInterpreter(),
               taskActorDeclaration.getSink());
    }

    private static void verify(Class<? extends Source> source,
                               Class<? extends Interpreter> interpreter,
                               Class<? extends Sink> sink) {
        SourceDeclaration sourceDeclaration = source.getAnnotation(SourceDeclaration.class);
        InterpreterDeclaration interpreterDeclaration = interpreter.getAnnotation(InterpreterDeclaration.class);
        SinkDeclaration sinkDeclaration = sink.getAnnotation(SinkDeclaration.class);
        if (interpreterDeclaration.takes() != sourceDeclaration.emits()) {
            throw new TeflonError(ErrorCode.INVALID_DECLARATION, "Interpreter must take what the Source provides");
        }
        if (interpreterDeclaration.emits() != sinkDeclaration.takes()) {
            throw new TeflonError(ErrorCode.INVALID_DECLARATION, "Sink must take what the Interpreter provides");
        }
    }

    private static FactoryProvider factoryProvider(FactoryType factoryType, AbstractModule abstractModule) {
        Preconditions.checkArgument(factoryType != FactoryType.INJECTION || abstractModule != null,
                                    "AbstractModule may not be null");
        if (factoryType == FactoryType.INJECTION) {
            return new InjectedFactoryProvider(abstractModule);
        } else {
            return new ReflectionFactoryProvider();
        }
    }
}

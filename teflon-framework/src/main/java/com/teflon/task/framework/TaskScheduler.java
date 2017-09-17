package com.teflon.task.core;

import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.teflon.task.core.container.MapContainer;
import com.teflon.task.core.core.ExecutionFactory;
import com.teflon.task.core.core.MetaInfo;
import com.teflon.task.core.error.ErrorCode;
import com.teflon.task.core.error.TeflonError;
import com.teflon.task.core.factory.FactoryProvider;
import com.teflon.task.core.factory.InjectedFactoryProvider;
import com.teflon.task.core.factory.ReflectionFactoryProvider;
import com.teflon.task.core.impl2.InterpreterDeclaration;
import com.teflon.task.core.impl2.SimpleImpl;
import com.teflon.task.core.impl2.SinkDeclaration;
import com.teflon.task.core.impl2.SourceDeclaration;
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


    public static void main(String[] args) {
        TaskScheduler sample = TaskScheduler.builder().declaration(TaskActorDeclaration.builder()
                                                                                       .name("sample")
                                                                                       .source(SimpleImpl.class)
                                                                                       .interpreter(SimpleImpl.class)
                                                                                       .sink(SimpleImpl.class)
                                                                                       .build())
                                            .build();
        sample.trigger(() -> "sample");

    }
}

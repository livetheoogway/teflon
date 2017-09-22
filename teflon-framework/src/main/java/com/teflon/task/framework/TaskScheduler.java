package com.teflon.task.framework;

import com.google.common.base.Strings;
import com.google.inject.AbstractModule;
import com.teflon.task.framework.container.MapContainer;
import com.teflon.task.framework.core.Task;
import com.teflon.task.framework.core.meta.MetaInfo;
import com.teflon.task.framework.core.meta.TaskStat;
import com.teflon.task.framework.declaration.TaskActorDeclaration;
import com.teflon.task.framework.declaration.annotated.TaskDeclaration;
import com.teflon.task.framework.error.ErrorCode;
import com.teflon.task.framework.error.TeflonError;
import com.teflon.task.framework.error.Verifier;
import com.teflon.task.framework.factory.*;
import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 6:57 PM
 */
@Slf4j
public final class TaskScheduler {

    private MapContainer<MetaInfo> mContainer;

    private TaskScheduler(MapContainer<MetaInfo> mContainer) {
        this.mContainer = mContainer;
    }

    public boolean trigger(Task task) throws TeflonError {
        return trigger(task, t -> {
            /* nothing to do here */
        });
    }

    public boolean trigger(Task task, Consumer<TaskStat> taskStatConsumer) throws TeflonError {
        return trigger(task, taskStatConsumer, () -> false);
    }

    public boolean trigger(Task task, Consumer<TaskStat> taskStatConsumer,
                           BooleanSupplier isCancelled) throws TeflonError {
        MetaInfo metaInfo = mContainer.get(task.name());
        if (metaInfo == null) {
            throw new TeflonError(ErrorCode.TASK_UNAVAILABLE, "Task:" + task.name() +
                    " not registered. Available tasks:" + mContainer.keys());
        }
        log.info("Executing task:{} id:{}" + task, UUID.randomUUID().toString());
        return new ExecutionFactory<>(metaInfo).newInstance().initiate(task, taskStatConsumer, isCancelled);
    }

    @Builder
    public TaskScheduler(@Singular List<TaskActorDeclaration> declarations,
                         AbstractModule abstractModule, String classPath) {
        mContainer = new MapContainer<>();
        declarations.forEach(k -> {
            MetaInfo metaInfo = getMetaInfo(abstractModule, k);
            mContainer.register(k.getName(), metaInfo);
        });
        if (!Strings.isNullOrEmpty(classPath)) {
            Reflections reflections = new Reflections(classPath);
            Set<Class<?>> taskDeclarations = reflections.getTypesAnnotatedWith(TaskDeclaration.class);
            taskDeclarations.forEach(k -> {
                TaskDeclaration taskDeclaration = k.getAnnotation(TaskDeclaration.class);
                TaskActorDeclaration actorDeclaration
                        = TaskActorDeclaration.builder()
                                              .name(taskDeclaration.name())
                                              .factoryType(taskDeclaration.factoryType())
                                              .source(taskDeclaration.source())
                                              .interpreter(taskDeclaration.interpreter())
                                              .sink(taskDeclaration.sink())
                                              .batchSize(taskDeclaration.batchSize())
                                              .build();
                MetaInfo metaInfo = getMetaInfo(abstractModule, actorDeclaration);
                mContainer.register(actorDeclaration.getName(), metaInfo);
            });
        }
        log.info("Registered tasks:" + mContainer.keys());
    }

    private MetaInfo getMetaInfo(AbstractModule abstractModule, TaskActorDeclaration k) {
        Verifier.verify(k);
        FactoryProvider factoryProvider = factoryProvider(k.getFactoryType(), abstractModule);
        return MetaInfo.builder()
                       .sourceInstanceFactory(factoryProvider.instanceFactory(k.getSource()))
                       .interpreterInstanceFactory(factoryProvider.instanceFactory(k.getInterpreter()))
                       .sinkInstanceFactory(factoryProvider.instanceFactory(k.getSink()))
                       .batchSize(k.getBatchSize())
                       .build();
    }

    private FactoryProvider factoryProvider(FactoryType factoryType, AbstractModule abstractModule) {
        Verifier.checkExpression(factoryType != FactoryType.INJECTION || abstractModule != null,
                                    "AbstractModule may not be null");
        if (factoryType == FactoryType.INJECTION) {
            return new InjectedFactoryProvider(abstractModule);
        } else {
            return new ReflectionFactoryProvider();
        }
    }
}

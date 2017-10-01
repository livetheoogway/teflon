package com.teflon.task.framework;

import com.google.common.base.Strings;
import com.google.inject.Injector;
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
import java.util.concurrent.*;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This is the Scheduler that will try to trigger a task
 *
 * @author tushar.naik
 * @version 1.0  15/09/17 - 6:57 PM
 */
@Slf4j
public final class TaskScheduler {

    /* container of registered tasks */
    private MapContainer<MetaInfo> mContainer;

    private ScheduledExecutorService executorService;

    @Builder.Default
    private int poolsize = 0;

    public TaskScheduler() {
        this(new MapContainer<>());
    }

    public TaskScheduler(MapContainer<MetaInfo> mContainer) {
        this.mContainer = mContainer;
    }

    /**
     * @param declarations     [optional] a bunch of task declarations
     * @param injectorProvider [optional] an injector
     * @param classPath        [optional] classPath the find annotated {@link TaskDeclaration}s
     */
    @Builder
    public TaskScheduler(@Singular List<TaskActorDeclaration> declarations,
                         Supplier<Injector> injectorProvider, String classPath,
                         int poolSize) {
        this.mContainer = new MapContainer<>();
        if (poolSize > 0) {
            executorService = Executors.newScheduledThreadPool(poolSize);
        }
        declarations.forEach(k -> {
            MetaInfo metaInfo = getMetaInfo(injectorProvider, k);
            this.mContainer.register(k.getName(), metaInfo);
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
                MetaInfo metaInfo = getMetaInfo(injectorProvider, actorDeclaration);
                this.mContainer.register(actorDeclaration.getName(), metaInfo);
            });
        }
        log.info("Registered tasks:" + mContainer.keys());
    }

    public boolean trigger(Task task) throws TeflonError {
        return trigger(task, t -> {
            /* nothing to do here */
        });
    }

    public boolean trigger(Task task, Consumer<TaskStat> taskStatConsumer) throws TeflonError {
        return trigger(task, taskStatConsumer, () -> false);
    }

    /**
     * trigger a task
     *
     * @param task             task to be executed
     * @param taskStatConsumer consumer of states which is called at regular inteval
     * @param isCancelled      to signal if the task needs to be cancelled
     * @return true if execution was successful
     * @throws TeflonError if there was an error
     */
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

    public Future<Boolean> submit(Task task, Consumer<TaskStat> taskStatConsumer, BooleanSupplier isCancelled) {
        preconditions();
        return executorService.submit(() -> trigger(task, taskStatConsumer, isCancelled));
    }

    public ScheduledFuture<Boolean> schedule(Task task, Consumer<TaskStat> taskStatConsumer,
                                             BooleanSupplier isCancelled, long delay, TimeUnit timeUnit) {
        preconditions();
        return executorService.schedule(() -> trigger(task, taskStatConsumer, isCancelled), delay, timeUnit);
    }

    public ScheduledFuture<?> scheduleAtFixedRate(Task task, Consumer<TaskStat> taskStatConsumer,
                                                  BooleanSupplier isCancelled, long delay, long interval,
                                                  TimeUnit timeUnit) {
        preconditions();
        return executorService.scheduleAtFixedRate(() -> trigger(task, taskStatConsumer, isCancelled), delay, interval, timeUnit);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////  HELPERS  ///////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////


    private void preconditions() {
        if (executorService == null) {
            throw new UnsupportedOperationException("PoolSize was not been initialized");
        }
    }

    private MetaInfo getMetaInfo(Supplier<Injector> injectorProvider, TaskActorDeclaration k) {
        Verifier.verify(k);
        FactoryProvider factoryProvider = factoryProvider(k.getFactoryType(), injectorProvider);
        return MetaInfo.builder()
                       .sourceInstanceFactory(factoryProvider.instanceFactory(k.getSource()))
                       .interpreterInstanceFactory(factoryProvider.instanceFactory(k.getInterpreter()))
                       .sinkInstanceFactory(factoryProvider.instanceFactory(k.getSink()))
                       .batchSize(k.getBatchSize())
                       .build();
    }

    private FactoryProvider factoryProvider(FactoryType factoryType, Supplier<Injector> injectorProvider) {
        Verifier.checkExpression(factoryType != FactoryType.INJECTION || injectorProvider != null,
                                 "AbstractModule may not be null");
        if (factoryType == FactoryType.INJECTION) {
            return new InjectedFactoryProvider(injectorProvider);
        } else {
            return new ReflectionFactoryProvider();
        }
    }
}

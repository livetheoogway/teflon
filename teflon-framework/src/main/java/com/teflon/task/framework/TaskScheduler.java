package com.teflon.task.framework;

import com.google.common.base.Strings;
import com.google.inject.Injector;
import com.teflon.task.framework.container.MapContainer;
import com.teflon.task.framework.core.Task;
import com.teflon.task.framework.core.meta.MetaInfo;
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

    /* executor service for concurrent task execution */
    private ScheduledExecutorService executorService;

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
        return trigger(task, new StatusCallback() {
        });
    }

    /**
     * trigger a task
     *
     * @param task           task to be executed
     * @param statusCallback consumer of execution lifecycle status, which is called at regular intervals
     * @return true if execution was successful
     * @throws TeflonError if there was an error
     */
    public boolean trigger(Task task, StatusCallback statusCallback) throws TeflonError {
        MetaInfo metaInfo = mContainer.get(task.name());
        if (metaInfo == null) {
            throw new TeflonError(ErrorCode.TASK_UNAVAILABLE, "Task:" + task.name() +
                    " not registered. Available tasks:" + mContainer.keys());
        }
        log.info("Executing task:{} id:{}" + task, UUID.randomUUID().toString());
        return new ExecutionFactory<>(metaInfo).newInstance().initiate(task, statusCallback);
    }

    /**
     * submit the execution of a task.
     * Task will be executed by the executor service in a background thread
     *
     * @param task           task to be executed
     * @param statusCallback consumer of execution lifecycle status, which is called at regular intervals
     * @return Future instance of the submission
     */
    public Future<Boolean> submit(Task task, StatusCallback statusCallback) {
        preconditions();
        return executorService.submit(() -> trigger(task, statusCallback));
    }


    /**
     * schedule the execution of a task after some delay
     * Task will be executed by the executor service in a background thread, after delay has passed
     *
     * @param task           supplier of task to be execute
     * @param statusCallback consumer of execution lifecycle status, which is called at regular intervals
     * @return Future instance of the submission
     */
    public ScheduledFuture<Boolean> schedule(Supplier<Task> task, StatusCallback statusCallback, long delay,
                                             TimeUnit timeUnit) {
        preconditions();
        return executorService.schedule(() -> trigger(task.get(), statusCallback), delay, timeUnit);
    }

    /**
     * Schedule the execution of a task
     * Task will be executed by the executor service in a background thread at regular intervals.
     * Starts after initial delay
     * triggers the next execution at 'interval' duration
     *
     * @param task           supplier of task to be executed
     * @param statusCallback consumer of execution lifecycle status, which is called at regular intervals
     * @param initialDelay   initial initialDelay
     * @param interval       the period between successive executions
     * @param timeUnit       the time unit of the initialDelay and interval parameters
     * @return Future instance of the submission
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Supplier<Task> task, StatusCallback statusCallback, long initialDelay,
                                                  long interval,
                                                  TimeUnit timeUnit) {
        preconditions();
        return executorService.scheduleAtFixedRate(() -> trigger(task.get(), statusCallback), initialDelay, interval, timeUnit);
    }

    /**
     * Schedule the execution of a task
     * Task will be executed by the executor service in a background thread at regular intervals.
     * Starts after initial delay
     * triggers next execution after 'delay' duration has passed from the last execution
     *
     * @param task           supplier of task to be executed
     * @param statusCallback consumer of execution lifecycle status, which is called at regular intervals
     * @param initialDelay   initial initialDelay
     * @param delay          the delay between the termination of one execution and the commencement of the next
     * @param timeUnit       the time unit of the initialDelay and delay parameters
     * @return Future instance of the submission
     */
    public ScheduledFuture<?> scheduleWithFixedDelay(Supplier<Task> task, StatusCallback statusCallback,
                                                     long initialDelay, long delay,
                                                     TimeUnit timeUnit) {
        preconditions();
        return executorService.scheduleWithFixedDelay(() -> trigger(task.get(), statusCallback), initialDelay, delay, timeUnit);
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

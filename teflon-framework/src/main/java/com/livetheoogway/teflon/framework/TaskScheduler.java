/*
 * Copyright 2022. Live the Oogway, Tushar Naik
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */

package com.livetheoogway.teflon.framework;

import com.google.common.base.Strings;
import com.google.inject.Injector;
import com.livetheoogway.teflon.framework.core.meta.MetaInfo;
import com.livetheoogway.teflon.framework.core.meta.TaskStat;
import com.livetheoogway.teflon.framework.error.ErrorCode;
import com.livetheoogway.teflon.framework.error.TeflonError;
import com.livetheoogway.teflon.framework.error.Verifier;
import com.livetheoogway.teflon.framework.factory.ExecutionFactory;
import com.livetheoogway.teflon.framework.factory.FactoryProvider;
import com.livetheoogway.teflon.framework.factory.FactoryType;
import com.livetheoogway.teflon.framework.factory.InjectedFactoryProvider;
import com.livetheoogway.teflon.framework.factory.ReflectionFactoryProvider;
import com.livetheoogway.teflon.framework.container.MapContainer;
import com.livetheoogway.teflon.framework.core.Task;
import com.livetheoogway.teflon.framework.declaration.TaskActorDeclaration;
import com.livetheoogway.teflon.framework.declaration.annotated.TaskDeclaration;
import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;
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
    private final MapContainer<MetaInfo> mContainer;

    /* executor service for concurrent task execution */
    private final ScheduledExecutorService executorService;

    /**
     * @param declarations     [optional] a bunch of task declarations
     * @param injectorProvider [optional] an injector
     * @param classPath        [optional] classPath the find annotated {@link TaskDeclaration}s
     * @param poolSize         [optional] pool size if you don't want single threaded execution
     */
    @Builder
    public TaskScheduler(@Singular final List<TaskActorDeclaration> declarations,
                         final Supplier<Injector> injectorProvider,
                         final String classPath,
                         final int poolSize) {
        this.mContainer = new MapContainer<>();
        if (poolSize > 0) {
            executorService = Executors.newScheduledThreadPool(poolSize);
        } else {
            executorService = Executors.newSingleThreadScheduledExecutor();
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

    /**
     * trigger a task
     *
     * @param task           task to be executed
     * @param statusCallback consumer of execution lifecycle status, which is called at regular intervals
     * @return true if execution was successful
     * @throws TeflonError if there was an error
     */
    public boolean trigger(final Task task, final StatusCallback statusCallback) {
        MetaInfo metaInfo = mContainer.get(task.name());
        if (metaInfo == null) {
            throw new TeflonError(ErrorCode.TASK_UNAVAILABLE, "Task:" + task.name() +
                    " not registered. Available tasks:" + mContainer.keys());
        }
        log.info("Executing task:{}", task);
        try {
            TaskExecutor<?, ?, ?> taskExecutor = new ExecutionFactory<>(metaInfo).newInstance();
            log.debug("taskExecutor:" + taskExecutor);
            return taskExecutor.initiate(task, statusCallback);
        } catch (Exception e) {
            log.error("Error while creating taskExecutor", e);
            return false;
        }
    }

    public boolean trigger(Task task) {
        return trigger(task, new StatusCallback() {
        });
    }

    /**
     * try and resume a task from its previous state (progress)
     *
     * @param task           task to be resumed
     * @param statusCallback status consumer
     * @param taskStat       stats (previous state) of the earlier running task
     * @return true if successful
     * @throws TeflonError while resuming task
     */
    public boolean resume(Task task, StatusCallback statusCallback, TaskStat taskStat) {
        MetaInfo metaInfo = mContainer.get(task.name());
        if (metaInfo == null) {
            throw new TeflonError(ErrorCode.TASK_UNAVAILABLE, "Task:" + task.name() +
                    " not registered. Available tasks:" + mContainer.keys());
        }
        log.info("Executing task:{}", task);
        try {
            TaskExecutor<?, ?, ?> taskExecutor = new ExecutionFactory<>(metaInfo).newInstance();
            log.debug("taskExecutor:" + taskExecutor);
            return taskExecutor.resume(task, statusCallback, taskStat);
        } catch (Exception e) {
            log.error("Error while creating taskExecutor", e);
            return false;
        }
    }

    public boolean resume(Task task, TaskStat<?> taskStat) {
        return resume(task, new StatusCallback() {
        }, taskStat);
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
        log.info("Submitting task:{}", task);
        return executorService.submit(() -> trigger(task, statusCallback));
    }

    public Future<Boolean> submit(Task task, StatusCallback statusCallback, TaskStat<?> taskStat) {
        preconditions();
        log.info("Submitting task:{}", task);
        return executorService.submit(() -> resume(task, statusCallback, taskStat));
    }

    /**
     * schedule the execution of a task after some delay.
     * Task will be executed by the executor service in a background thread, after delay has passed
     *
     * @param task           supplier of task to be executed
     * @param statusCallback consumer of execution lifecycle status, which is called at regular intervals
     * @param delay          delay before schedule
     * @param timeUnit       unit of the delay mentioned
     * @return Future instance of the submission
     */
    public ScheduledFuture<Boolean> schedule(Supplier<Task> task, StatusCallback statusCallback, long delay,
                                             TimeUnit timeUnit) {
        preconditions();
        log.info("Scheduling task:{}", task);
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
    public ScheduledFuture scheduleAtFixedRate(Supplier<Task> task, StatusCallback statusCallback, long initialDelay,
                                               long interval, TimeUnit timeUnit) {
        preconditions();
        log.info("Scheduling at Fixed Rate- task:{}", task);
        return executorService.scheduleAtFixedRate(() -> trigger(task.get(), statusCallback), initialDelay, interval,
                                                   timeUnit);
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
    public ScheduledFuture scheduleWithFixedDelay(Supplier<Task> task, StatusCallback statusCallback,
                                                  long initialDelay, long delay,
                                                  TimeUnit timeUnit) {
        preconditions();
        return executorService.scheduleWithFixedDelay(() -> trigger(task.get(), statusCallback), initialDelay, delay,
                                                      timeUnit);
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

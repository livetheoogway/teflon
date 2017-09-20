package com.teflon.task.rmq.actor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teflon.task.framework.TaskScheduler;
import com.teflon.task.framework.core.Task;
import io.dropwizard.actors.actor.Actor;
import io.dropwizard.actors.actor.ActorConfig;
import io.dropwizard.actors.connectivity.RMQConnection;
import io.dropwizard.actors.retry.RetryStrategyFactory;
import lombok.Builder;

import java.util.Set;

/**
 * @author tushar.naik
 * @version 1.0  19/09/17 - 10:18 PM
 */
public class TaskActor<T extends Enum<T>, M extends Task> extends Actor<T, M> {

    private TaskScheduler taskScheduler;

    @Builder
    public TaskActor(T t, TaskScheduler taskScheduler, ActorConfig config,
                     RMQConnection connection, ObjectMapper mapper,
                     RetryStrategyFactory retryStrategyFactory,
                     Class<? extends M> clazz, Set<Class<?>> droppedExceptionTypes) {
        super(t, config, connection, mapper, retryStrategyFactory, clazz, droppedExceptionTypes);
        this.taskScheduler = taskScheduler;
    }

    @Override
    protected boolean handle(M message) throws Exception {
        return taskScheduler.trigger(message);
    }

    public void trigger(M task) throws Exception {
        publish(task);
    }
}

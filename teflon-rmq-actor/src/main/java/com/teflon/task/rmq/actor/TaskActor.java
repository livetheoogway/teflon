package com.teflon.task.rmq.actor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teflon.task.framework.TaskScheduler;
import com.teflon.task.framework.core.Task;
import com.teflon.task.framework.core.meta.TaskStat;
import io.dropwizard.actors.actor.Actor;
import io.dropwizard.actors.connectivity.RMQConnection;
import io.dropwizard.actors.retry.RetryStrategyFactory;
import lombok.Builder;

import java.util.Collections;
import java.util.function.Consumer;

/**
 * @author tushar.naik
 * @version 1.0  19/09/17 - 10:18 PM
 */
public class TaskActor<T extends Enum<T>, M extends Task> extends Actor<T, M> {

    private TaskScheduler taskScheduler;

    @Builder
    public TaskActor(T t, TaskScheduler taskScheduler, TeflonConfig teflonConfig,
                     RMQConnection connection, ObjectMapper mapper,
                     Class<? extends M> clazz) {
        super(t, teflonConfig.getActorConfig(), connection, mapper, new RetryStrategyFactory(), clazz, Collections.emptySet());
        this.taskScheduler = taskScheduler;
    }

    public Consumer<TaskStat> statConsumer() {
        return taskStat -> { };
    }

    @Override
    protected final boolean handle(M message) throws Exception {
        return taskScheduler.trigger(message, statConsumer());
    }
}

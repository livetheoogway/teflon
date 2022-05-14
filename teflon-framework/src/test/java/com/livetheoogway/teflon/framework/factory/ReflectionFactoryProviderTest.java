package com.livetheoogway.teflon.framework.factory;

import com.livetheoogway.teflon.framework.StatusCallback;
import com.livetheoogway.teflon.framework.TaskScheduler;
import com.livetheoogway.teflon.framework.core.Task;
import com.livetheoogway.teflon.framework.core.meta.TaskStat;
import com.livetheoogway.teflon.framework.impl.ConsoleSink;
import com.livetheoogway.teflon.framework.declaration.TaskActorDeclaration;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tushar.naik
 * @version 1.0  19/09/17 - 9:22 PM
 */
public class ReflectionFactoryProviderTest {
    @Test
    public void testDefaultConstructor() {
        TaskScheduler taskScheduler = TaskScheduler.builder()
                .declaration(TaskActorDeclaration.builder().name("number-generator")
                                     .source(NumberStreamGenerator.class)
                                     .interpreter(NumberStreamGenerator.class)
                                     .sink(ConsoleSink.class)
                                     .build())
                .build();
        AtomicReference<TaskStat> taskStat = new AtomicReference<>();
        assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 5), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat taskStats) {
                taskStat.set(taskStats);
            }
        }));
//        assertEquals(taskStat.get().getCountTotal(), 5);
//        assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 3), new StatusCallback() {
//            @Override
//            public void statusCallback(Task task, TaskStat taskStats) {
//                taskStat.set(taskStats);
//            }
//        }));
//        assertEquals(taskStat.get().getCountTotal(), 3);
    }
}
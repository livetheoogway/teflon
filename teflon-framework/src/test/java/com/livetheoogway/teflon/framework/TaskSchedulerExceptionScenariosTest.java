package com.livetheoogway.teflon.framework;

import com.livetheoogway.teflon.framework.core.Task;
import com.livetheoogway.teflon.framework.core.meta.TaskStat;
import com.livetheoogway.teflon.framework.error.TeflonError;
import com.livetheoogway.teflon.framework.factory.NumberGeneratorTask;
import com.livetheoogway.teflon.framework.factory.NumberStreamGenerator;
import com.livetheoogway.teflon.framework.impl.ConsoleSink;
import com.livetheoogway.teflon.framework.declaration.TaskActorDeclaration;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tushar.naik
 * @version 1.0  18/09/17 - 11:54 AM
 */
class TaskSchedulerExceptionScenariosTest {

    @Test
    void testNullSource() {
        assertThrows(TeflonError.class,
                     () -> TaskScheduler.builder()
                             .declaration(TaskActorDeclaration.builder().name("number-generator").build())
                             .build());
    }


    @Test
    void testNullInterpreter() {
        assertThrows(TeflonError.class,
                     () -> TaskScheduler.builder().declaration(TaskActorDeclaration.builder().name("number-generator")
                                                                       .source(NumberStreamGenerator.class)
                                                                       .build()).build());
    }

    @Test
    void testAbstractModuleAbsent() {
        assertThrows(TeflonError.class,
                     () -> TaskScheduler.builder().classPath("com.teflon.task.framework.factory").build());
    }

    @Test
    void testDefaultConstructorError() {
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
        assertEquals(taskStat.get().getCountTotal(), 5);
        assertTrue(taskScheduler.trigger(new NumberGeneratorTask(1, 3), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat taskStats) {
                taskStat.set(taskStats);
            }
        }));
        assertEquals(taskStat.get().getCountTotal(), 3);
    }
}
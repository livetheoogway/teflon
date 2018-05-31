package com.teflon.task.framework;

import com.teflon.task.framework.core.Task;
import com.teflon.task.framework.core.meta.TaskStat;
import com.teflon.task.framework.factory.NumberGeneratorTask;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author tushar.naik
 * @version 1.0  02/10/17 - 2:21 AM
 */
public class ScheduledExecutionTest {

    private TaskScheduler taskScheduler;

    @Before
    public void setUp() {
        taskScheduler = TestUtil.getScheduler();
    }

    @Test
    public void testScheduledExecution() throws Exception {
        AtomicReference<TaskStat<Void>> taskStat = new AtomicReference<>();
        Random random = new Random();

        taskScheduler.scheduleAtFixedRate(() -> new NumberGeneratorTask(1, random.nextInt(10) + 2), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                taskStat.set(t);
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);

        Thread.sleep(3000);
        Assert.assertTrue(taskStat.get().getCountTotal() > 1);
    }

    @Test
    public void testScheduledFixedExecution() throws Exception {
        AtomicReference<TaskStat<Void>> taskStat = new AtomicReference<>();
        Random random = new Random();

        taskScheduler.scheduleWithFixedDelay(() -> new NumberGeneratorTask(1, random.nextInt(10) + 2), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                taskStat.set(t);
            }
        }, 20, 1000, TimeUnit.MILLISECONDS);

        Thread.sleep(3000);
        Assert.assertTrue(taskStat.get().getCountTotal() > 1);
    }

    @Test
    public void testScheduleExecution() throws Exception {
        AtomicReference<TaskStat<Void>> taskStat = new AtomicReference<>();
        Random random = new Random();

        taskScheduler.schedule(() -> new NumberGeneratorTask(1, random.nextInt(10) + 2), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                taskStat.set(t);
            }
        }, 100, TimeUnit.MILLISECONDS);
        Thread.sleep(20);
        Assert.assertTrue(taskStat.get() == null);

        Thread.sleep(1200);
        Assert.assertTrue(taskStat.get().getCountTotal() > 1);
    }

    @Test
    public void testSubmit() throws Exception {
        AtomicReference<TaskStat<Void>> taskStat = new AtomicReference<>();
        Random random = new Random();
        taskScheduler.submit(new NumberGeneratorTask(1, random.nextInt(10) + 2), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                taskStat.set(t);
            }
        });
        Assert.assertTrue(taskStat.get() == null);

        Thread.sleep(1000);
        Assert.assertTrue(taskStat.get().getCountTotal() > 1);

    }

    @Test
    public void testSubmitWithResume() {
        AtomicReference<TaskStat<Void>> taskStat = new AtomicReference<>();
        Random random = new Random();

        taskScheduler.trigger(new NumberGeneratorTask(1, random.nextInt(10) + 2), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                taskStat.set(t);
            }
        });
        long countPrevious = taskStat.get().getCountOutputSunk();
        System.out.println("countPrevious = " + countPrevious);
        taskScheduler.resume(new NumberGeneratorTask(1, random.nextInt(10) + 4), new StatusCallback() {
            @Override
            public void statusCallback(Task task, TaskStat t) {
                taskStat.set(t);
            }
        }, taskStat.get());
        System.out.println("countNow = " + taskStat.get().getCountOutputSunk());
        Assert.assertTrue(countPrevious < taskStat.get().getCountOutputSunk());
    }
}

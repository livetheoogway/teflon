package com.teflon.task.framework.core.meta;

import lombok.*;

/**
 * @author tushar.naik
 * @version 1.0  14/08/17 - 6:09 PM
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class TaskStat<Progress> {
    private long countTotal;
    private long countOutputSunk;
    private long startTime;
    private long endTime;
    private String totalTime;
    private Progress taskProgress;

    public TaskStat(long countTotal, long countOutputSunk) {
        this.countTotal = countTotal;
        this.countOutputSunk = countOutputSunk;
        start();
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.endTime = -1;
        this.totalTime = "";
    }

    public void end() {
        if (endTime > 0) {
            throw new RuntimeException("End has already been called once");
        }
        this.endTime = System.currentTimeMillis();
        this.totalTime = endTime - startTime + "ms";
    }

    /**
     * @return elapsed time, if end is not yet called for the stat
     */
    public String getElapsedTime() {
        if (endTime < 0) {
            return System.currentTimeMillis() - startTime + "ms";
        }
        return totalTime;
    }

    public void add(TaskStat taskStat) {
        this.startTime = Math.min(startTime, taskStat.startTime);
        this.endTime = Math.max(endTime, taskStat.endTime);
        this.countTotal += taskStat.countTotal;
        this.countOutputSunk += taskStat.countOutputSunk;
        this.totalTime = endTime - startTime + "ms";
    }
}

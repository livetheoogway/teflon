package com.teflon.task.framework.core.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tushar.naik
 * @version 1.0  14/08/17 - 6:09 PM
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskStat {
    private long countTotal;
    private long countOutputSinked;
    private long startTime;
    private long endTime;
    private String totalTime;

    public TaskStat(long countTotal, long countOutputSinked) {
        this.countTotal = countTotal;
        this.countOutputSinked = countOutputSinked;
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
        this.countOutputSinked += taskStat.countOutputSinked;
        this.totalTime = endTime - startTime + "ms";
    }
}

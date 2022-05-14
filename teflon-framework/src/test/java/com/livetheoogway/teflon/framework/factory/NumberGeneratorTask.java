package com.livetheoogway.teflon.framework.factory;

import com.livetheoogway.teflon.framework.core.Task;
import lombok.Data;

/**
 * @author tushar.naik
 * @version 1.0  19/09/17 - 7:32 PM
 */
@Data
public class NumberGeneratorTask implements Task {
    private int start;
    private int end;

    public NumberGeneratorTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String name() {
        return "number-generator";
    }
}

package com.teflon.task.core;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * @author tushar.naik
 * @version 1.0  16/08/17 - 1:43 AM
 */
public abstract class BatchedSource<Input> implements Source<Input> {

    private Queue<Input> hitQueue;

    public BatchedSource() {
        this.hitQueue = new LinkedList<>();
    }

    public abstract Collection<Input> getInputs();

    public abstract Collection<Input> batchInit(Task init);

    @Override
    public void init(Task init) throws Exception {
        Collection<Input> initialInputs = batchInit(init);
        if (initialInputs != null && !initialInputs.isEmpty()) {
            hitQueue.addAll(initialInputs);
        }
    }

    @Override
    public Input getInput() throws Exception {
        Input input = hitQueue.poll();
        if (input == null) {
            Collection<Input> inputs = getInputs();
            if (inputs == null || inputs.isEmpty()) {
                return null;
            }
            hitQueue.addAll(inputs);
            input = hitQueue.poll();
        }
        return input;
    }

    @Override
    public void close() throws IOException {
        hitQueue.clear();
    }

    @Override
    public void abort() {
        hitQueue.clear();
    }
}

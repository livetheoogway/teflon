package com.teflon.task.framework.impl;

import com.teflon.task.framework.core.Sink;

import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 7:53 PM
 */
public class ConsoleSink<T> implements Sink<T> {
    @Override
    public void init() throws Exception {

    }

    @Override
    public void sink(T item) {
        System.out.println(item);
    }

    @Override
    public void sink(List<T> items) {
        System.out.println(items);
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void abort() {

    }
}

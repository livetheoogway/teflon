package com.teflon.task.core.example;

import com.teflon.task.core.Sink;

import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 7:53 PM
 */
public class ConsoleSink implements Sink<Integer> {
    @Override
    public void init() throws Exception {

    }

    @Override
    public void sink(Integer integer) {
        System.out.println("integer = " + integer);
    }

    @Override
    public void sink(List<Integer> integers) {
        System.out.println("integers = " + integers);
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public void abort() {

    }
}

package com.teflon.task.core;

import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  10/08/17 - 6:05 PM
 */
public interface Sink<Output> {

    /**
     * initiate sink purpose
     */
    void init() throws Exception;

    /**
     * save an incoming output
     *
     * @param output output after interpretation
     */
    void sink(Output output);

    /**
     * save an incoming output
     *
     * @param outputs output after interpretation
     */
    void sink(List<Output> outputs);

    /**
     * close the initiation
     */
    void close() throws Exception;

    /**
     * abort the population. rollback
     */
    void abort();
}

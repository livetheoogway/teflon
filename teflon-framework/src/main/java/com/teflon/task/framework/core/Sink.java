package com.teflon.task.framework.core;

import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  10/08/17 - 6:05 PM
 */
public interface Sink<Output> extends Unit {

    /**
     * sink an incoming output
     *
     * @param output output after interpretation
     */
    void sink(Output output);

    /**
     * sink a bunch of outputs
     *
     * @param outputs outputs after interpretation
     */
    void sink(List<Output> outputs);

}

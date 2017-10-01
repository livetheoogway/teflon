package com.teflon.task.framework.core;

import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  10/08/17 - 6:05 PM
 */
public interface Interpreter<Input, Output> extends Unit {
    /**
     * interprets a bunch of inputs into some form of an Output
     *
     * @param inputs list of inputs coming from the source
     * @return interpreted value
     */
    List<Output> interpret(List<Input> inputs);
}

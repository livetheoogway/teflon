package com.teflon.task.framework.core;

/**
 * @author tushar.naik
 * @version 1.0  10/08/17 - 6:05 PM
 */
public interface Interpreter<Input, Output> extends Unit {
    /**
     * interpret an intput into some form of an Output
     *
     * @param input any input
     * @return interpreted value
     */
    Output interpret(Input input);
}

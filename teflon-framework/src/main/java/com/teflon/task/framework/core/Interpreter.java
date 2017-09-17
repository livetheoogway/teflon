package com.teflon.task.core;

/**
 * @author tushar.naik
 * @version 1.0  10/08/17 - 6:05 PM
 */
@FunctionalInterface
public interface Interpreter<Input, Output> {

    /**
     * individual string can be used to interpret
     *
     * @param line any string
     * @return interpreted value
     */
    Output interpret(Input line);
}

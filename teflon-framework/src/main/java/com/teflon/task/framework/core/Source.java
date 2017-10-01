package com.teflon.task.framework.core;

import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  10/08/17 - 6:05 PM
 */
public interface Source<Input> extends Unit {

    /**
     * get a single InterimOutput from Source
     * returns null if stream is empty
     *
     * @return Input
     * @throws Exception while getting input
     */
    List<Input> getInput() throws Exception;
}

package com.teflon.task.framework.core;

import java.io.IOException;

/**
 * @author tushar.naik
 * @version 1.0  10/08/17 - 6:05 PM
 */
public interface Source<Input> {

    /**
     * initiate for reading InterimOutputs as a stream for a given Input
     *
     * @throws Exception
     */
    void init(Task init) throws Exception;

    /**
     * get a single InterimOutput from Source
     * returns null if stream is empty
     *
     * @return Input
     * @throws Exception
     */
    Input getInput() throws Exception;

    /**
     * close the initiation
     *
     * @throws IOException
     */
    void close() throws IOException;

    /**
     * abort the population. rollback
     */
    void abort();

}

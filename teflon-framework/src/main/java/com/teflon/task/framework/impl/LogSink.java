package com.teflon.task.framework.impl;

import com.teflon.task.framework.core.Sink;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  17/09/17 - 11:59 PM
 */
@Slf4j
public class LogSink<T> implements Sink<T> {

    @Override
    public void sink(T t) {
        log.info(t.toString());
    }

    @Override
    public void sink(List<T> ts) {
        log.info(ts.toString());
    }
}

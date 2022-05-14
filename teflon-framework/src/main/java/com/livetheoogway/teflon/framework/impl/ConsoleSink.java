package com.livetheoogway.teflon.framework.impl;

import com.livetheoogway.teflon.framework.core.Sink;
import com.livetheoogway.teflon.framework.declaration.annotated.SinkDeclaration;

import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 7:53 PM
 */
@SinkDeclaration(takes = String.class)
public class ConsoleSink implements Sink<String> {

    @Override
    public void sink(List<String> items) {
        items.forEach(System.out::println);
    }
}

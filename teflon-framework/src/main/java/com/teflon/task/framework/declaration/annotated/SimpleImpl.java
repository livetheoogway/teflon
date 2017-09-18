package com.teflon.task.framework.declaration.annotated;

import com.teflon.task.framework.core.Interpreter;
import com.teflon.task.framework.core.Sink;
import com.teflon.task.framework.core.Source;
import com.teflon.task.framework.core.Task;
import com.teflon.task.framework.factory.FactoryType;

import java.io.IOException;
import java.util.List;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 12:26 AM
 */
@SourceDeclaration(emits = String.class)
@InterpreterDeclaration(takes = String.class, emits = Integer.class)
@SinkDeclaration(takes = Integer.class)
@TaskDeclaration(name = "simple", source = SimpleImpl.class,
        interpreter = SimpleImpl.class,
        sink = SimpleImpl.class,
        factoryType = FactoryType.DEFAULT_CONSTRUCTOR_REFLECTION)
public class SimpleImpl implements Source<String>, Interpreter<String, Integer>, Sink<Integer> {
    int i = 0;

    @Override
    public Integer interpret(String line) {
        return line.hashCode();
    }

    @Override
    public void init(Task init) throws Exception {

    }

    @Override
    public String getInput() throws Exception {
        if (i++ < 30)
            return "asdf";
        return null;
    }

    @Override
    public void init() throws Exception {
        System.out.println("init = ");
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
    public void close() throws IOException {

    }

    @Override
    public void abort() {

    }
}

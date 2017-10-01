package com.teflon.task.framework.factory;

import com.teflon.task.framework.core.Interpreter;
import com.teflon.task.framework.core.Source;
import com.teflon.task.framework.core.Task;
import com.teflon.task.framework.declaration.annotated.InterpreterDeclaration;
import com.teflon.task.framework.declaration.annotated.SinkDeclaration;
import com.teflon.task.framework.declaration.annotated.SourceDeclaration;
import com.teflon.task.framework.declaration.annotated.TaskDeclaration;
import com.teflon.task.framework.impl.ConsoleSink;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 12:26 AM
 */
@SourceDeclaration(emits = Integer.class)
@InterpreterDeclaration(takes = Integer.class, emits = String.class)
@SinkDeclaration(takes = Integer.class)
@TaskDeclaration(name = "number-generator", source = NumberStreamGenerator.class,
        interpreter = NumberStreamGenerator.class,
        sink = ConsoleSink.class,
        factoryType = FactoryType.INJECTION)
public class NumberStreamGenerator implements Source<Integer>, Interpreter<Integer, String> {
    private int i;
    private int max;

    @Override
    public void init(Task init) throws Exception {
        NumberGeneratorTask taskGen = (NumberGeneratorTask) init;
        i = taskGen.getStart();
        max = taskGen.getEnd();
    }

    @Override
    public List<Integer> getInput() throws Exception {
        if (i <= max)
            return Collections.singletonList(i++);
        return null;
    }

    @Override
    public List<String> interpret(List<Integer> integer) {
        return integer.stream().map(k->"Iteration: " + integer).collect(Collectors.toList());
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void abort() {
    }
}

package com.teflon.task.framework.factory;

import com.teflon.task.framework.core.Interpreter;
import com.teflon.task.framework.core.Source;
import com.teflon.task.framework.core.SourceInputs;
import com.teflon.task.framework.core.Task;
import com.teflon.task.framework.core.meta.TaskStat;
import com.teflon.task.framework.declaration.annotated.InterpreterDeclaration;
import com.teflon.task.framework.declaration.annotated.SinkDeclaration;
import com.teflon.task.framework.declaration.annotated.SourceDeclaration;
import com.teflon.task.framework.declaration.annotated.TaskDeclaration;
import com.teflon.task.framework.impl.ConsoleSink;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
public class NumberStreamGenerator
        implements Source<Integer, NumberStreamGenerator.NSProgress>, Interpreter<Integer, String> {

    @AllArgsConstructor
    class NSProgress {
        @Getter
        private int resumeFrom;
    }

    private int i;
    private int max;

    @Override
    public void init(Task init) {
        NumberGeneratorTask taskGen = (NumberGeneratorTask) init;
        i = taskGen.getStart();
        max = taskGen.getEnd();
    }

    @Override
    public void resume(Task init, TaskStat<NumberStreamGenerator.NSProgress> taskStat) {
        NumberGeneratorTask taskGen = (NumberGeneratorTask) init;
        i = taskStat.getTaskProgress().getResumeFrom();
        max = taskGen.getEnd();
    }

    @Override
    public SourceInputs<Integer, NSProgress> getInput() {
        if (i <= max)
            return new SourceInputs<>(Collections.singletonList(i++), new NumberStreamGenerator.NSProgress(max / 2));
        return null;
    }

    @Override
    public List<String> interpret(List<Integer> integer) {
        return integer.stream().map(k -> "Iteration: " + integer).collect(Collectors.toList());
    }

    @Override
    public void close() {
    }

    @Override
    public void abort() {
    }
}

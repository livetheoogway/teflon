package com.livetheoogway.teflon.framework.factory;

import com.livetheoogway.teflon.framework.core.Interpreter;
import com.livetheoogway.teflon.framework.core.Source;
import com.livetheoogway.teflon.framework.core.SourceInputs;
import com.livetheoogway.teflon.framework.core.Task;
import com.livetheoogway.teflon.framework.core.meta.TaskStat;
import com.livetheoogway.teflon.framework.impl.ConsoleSink;
import com.livetheoogway.teflon.framework.declaration.annotated.InterpreterDeclaration;
import com.livetheoogway.teflon.framework.declaration.annotated.SinkDeclaration;
import com.livetheoogway.teflon.framework.declaration.annotated.SourceDeclaration;
import com.livetheoogway.teflon.framework.declaration.annotated.TaskDeclaration;
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
    public void init(Task task) {
        NumberGeneratorTask taskGen = (NumberGeneratorTask) task;
        i = taskGen.getStart();
        max = taskGen.getEnd();
    }

    @Override
    public void resume(Task task, TaskStat<NSProgress> taskStat) {
        NumberGeneratorTask taskGen = (NumberGeneratorTask) task;
        i = taskStat.getTaskProgress().getResumeFrom();
        max = taskGen.getEnd();
    }

    @Override
    public SourceInputs<Integer, NSProgress> getInput() {
        if (i <= max)
            return new SourceInputs<>(Collections.singletonList(i++), new NumberStreamGenerator.NSProgress(1));
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

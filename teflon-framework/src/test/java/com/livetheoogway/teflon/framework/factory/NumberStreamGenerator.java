/*
 * Copyright 2022. Live the Oogway, Tushar Naik
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and limitations
 * under the License.
 */

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

    public record NSProgress(int resumeFrom) {}

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
        i = taskStat.getTaskProgress().resumeFrom();
        max = taskGen.getEnd();
    }

    @Override
    public SourceInputs<Integer, NSProgress> getInput() {
        if (i <= max) {
            return new SourceInputs<>(Collections.singletonList(i++), new NumberStreamGenerator.NSProgress(1));
        }
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

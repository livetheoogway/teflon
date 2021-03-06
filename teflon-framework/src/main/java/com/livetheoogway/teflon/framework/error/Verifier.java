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

package com.livetheoogway.teflon.framework.error;

import com.livetheoogway.teflon.framework.core.Interpreter;
import com.livetheoogway.teflon.framework.core.Sink;
import com.livetheoogway.teflon.framework.core.Source;
import com.livetheoogway.teflon.framework.declaration.TaskActorDeclaration;
import com.livetheoogway.teflon.framework.declaration.annotated.InterpreterDeclaration;
import com.livetheoogway.teflon.framework.declaration.annotated.SinkDeclaration;
import com.livetheoogway.teflon.framework.declaration.annotated.SourceDeclaration;

import javax.annotation.Nullable;

/**
 * @author tushar.naik
 * @version 1.0  19/09/17 - 7:57 PM
 */
public interface Verifier {

    /**
     * verify is a task declaration in valid
     *
     * @param taskActorDeclaration task declaration
     */
    static void verify(final TaskActorDeclaration taskActorDeclaration) {
        verify(taskActorDeclaration.getName(),
               taskActorDeclaration.getSource(),
               taskActorDeclaration.getInterpreter(),
               taskActorDeclaration.getSink());
    }

    /**
     * verifies if a proper chain is formed
     * The {@link Source} must emit a valid
     * The {@link Interpreter} must take what the {@link Source} emits
     * The {@link Sink} must take what the {@link Interpreter} emits
     *
     * @param name        name of the task
     * @param source      source class
     * @param interpreter interpreter class
     * @param sink        sink class
     */
    static void verify(final String name,
                       final Class<? extends Source<?, ?>> source,
                       final Class<? extends Interpreter<?, ?>> interpreter,
                       final Class<? extends Sink<?>> sink) {
        checkNull(source, message(name, source, interpreter, sink, "Source cannot be null"));
        checkNull(interpreter, message(name, source, interpreter, sink, "Interpreter cannot be null"));
        checkNull(sink, message(name, source, interpreter, sink, "Sink cannot be null"));
        SourceDeclaration sourceDeclaration = source.getAnnotation(SourceDeclaration.class);
        InterpreterDeclaration interpreterDeclaration = interpreter.getAnnotation(InterpreterDeclaration.class);
        SinkDeclaration sinkDeclaration = sink.getAnnotation(SinkDeclaration.class);
        if (interpreterDeclaration.takes() != sourceDeclaration.emits()) {
            throw new TeflonError(ErrorCode.INVALID_DECLARATION, message(name, source, interpreter, sink,
                                                                         "Interpreter must take what the Source "
                                                                                 + "provides"));
        }
        if (interpreterDeclaration.emits() != sinkDeclaration.takes()) {
            throw new TeflonError(ErrorCode.INVALID_DECLARATION, message(name, source, interpreter, sink,
                                                                         "Sink must take what the Interpreter "
                                                                                 + "provides"));
        }
    }

    static void checkExpression(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new TeflonError(ErrorCode.INVALID_DECLARATION, String.valueOf(errorMessage));
        }
    }

    static String message(final String name,
                          final Class<? extends Source<?, ?>> source,
                          final Class<? extends Interpreter<?, ?>> interpreter,
                          final Class<? extends Sink<?>> sink,
                          final String message) {
        return String.format("declaration:%s source:%s interpreter:%s sink:%s error:%s", name, source, interpreter,
                             sink, message);
    }

    static <T> void checkNull(T o, String message) {
        if (o == null) {
            throw new TeflonError(ErrorCode.INVALID_DECLARATION, message);
        }
    }
}

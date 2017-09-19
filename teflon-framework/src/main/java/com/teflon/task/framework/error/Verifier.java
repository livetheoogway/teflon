package com.teflon.task.framework.error;

import com.teflon.task.framework.core.Interpreter;
import com.teflon.task.framework.core.Sink;
import com.teflon.task.framework.core.Source;
import com.teflon.task.framework.declaration.TaskActorDeclaration;
import com.teflon.task.framework.declaration.annotated.InterpreterDeclaration;
import com.teflon.task.framework.declaration.annotated.SinkDeclaration;
import com.teflon.task.framework.declaration.annotated.SourceDeclaration;

import javax.annotation.Nullable;

/**
 * @author tushar.naik
 * @version 1.0  19/09/17 - 7:57 PM
 */
public interface Verifier {

    static void verify(TaskActorDeclaration taskActorDeclaration) {
        verify(taskActorDeclaration.getSource(),
               taskActorDeclaration.getInterpreter(),
               taskActorDeclaration.getSink());
    }

    static void verify(Class<? extends Source> source,
                       Class<? extends Interpreter> interpreter,
                       Class<? extends Sink> sink) {
        checkNull(source, "Source cannot be null");
        checkNull(interpreter, "Interpreter cannot be null");
        checkNull(sink, "Sink cannot be null");
        SourceDeclaration sourceDeclaration = source.getAnnotation(SourceDeclaration.class);
        InterpreterDeclaration interpreterDeclaration = interpreter.getAnnotation(InterpreterDeclaration.class);
        SinkDeclaration sinkDeclaration = sink.getAnnotation(SinkDeclaration.class);
        if (interpreterDeclaration.takes() != sourceDeclaration.emits()) {
            throw new TeflonError(ErrorCode.INVALID_DECLARATION, "Interpreter must take what the Source provides");
        }
        if (interpreterDeclaration.emits() != sinkDeclaration.takes()) {
            throw new TeflonError(ErrorCode.INVALID_DECLARATION, "Sink must take what the Interpreter provides");
        }
    }

    static void checkExpression(boolean expression, @Nullable Object errorMessage) {
        if (!expression) {
            throw new TeflonError(ErrorCode.INVALID_DECLARATION, String.valueOf(errorMessage));
        }
    }

    static <T> void checkNull(T o, String message) {
        if (o == null) {
            throw new TeflonError(ErrorCode.INVALID_DECLARATION, message);
        }
    }
}

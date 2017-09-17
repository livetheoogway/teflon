package com.teflon.task.core;

import com.teflon.task.core.error.ErrorCode;
import com.teflon.task.core.error.TeflonError;
import com.teflon.task.core.impl2.InterpreterDeclaration;
import com.teflon.task.core.impl2.SinkDeclaration;
import com.teflon.task.core.impl2.SourceDeclaration;
import org.reflections.Reflections;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 7:49 PM
 */
public class TaskExecutorProvider {

    public static <U, V> TaskExecutor<U, V> getTaskExecutor() throws Exception {
        Reflections reflections = new Reflections("com.teflon.task.core");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(TaskDeclaration.class);
        Class<?> next = annotated.iterator().next();
        Class<? extends Interpreter> interpreter = next.getAnnotation(TaskDeclaration.class).interpreter();
        Class<? extends Sink> sink = next.getAnnotation(TaskDeclaration.class).sink();
        Class<? extends Source> source = next.getAnnotation(TaskDeclaration.class).source();

//
//        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(TaskDefinition.class);
//        annotated.stream().map(k->k.getAnnotation(TaskDefinition.class)).collect(Collectors.groupingBy(k->k.ge));

        Type[] genericInterfaces = source.getGenericInterfaces();
        System.out.println("genericInterfaces = " + Arrays.toString(genericInterfaces));
        Source<U> source1 = source.getDeclaredConstructor().newInstance();
        Interpreter<U, V> interpreter1 = interpreter.getDeclaredConstructor().newInstance();
        Sink<V> sink1 = sink.getDeclaredConstructor().newInstance();
        return TaskExecutor.<U, V>builder().source(source1)
                                           .interpreter(interpreter1)
                                           .batchSize(11)
                                           .sink(sink1).build();
    }


    public static <U, V> TaskExecutor<U, V> getTaskExecutor2() throws Exception {
        Reflections reflections = new Reflections("com.teflon.task.core");
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(TaskDeclaration.class);
        Class<?> next = annotated.iterator().next();
        Class<? extends Source> source = next.getAnnotation(TaskDeclaration.class).source();
        SourceDeclaration sourceDeclaration = source.getAnnotation(SourceDeclaration.class);

        Class<? extends Interpreter> interpreter = next.getAnnotation(TaskDeclaration.class).interpreter();
        InterpreterDeclaration interpreterDeclaration = interpreter.getAnnotation(InterpreterDeclaration.class);
        if (interpreterDeclaration.takes() != sourceDeclaration.emits()) {
            throw new RuntimeException("Cannot");
        }
        Class<? extends Sink> sink = next.getAnnotation(TaskDeclaration.class).sink();
        SinkDeclaration sinkDeclaration = sink.getAnnotation(SinkDeclaration.class);
        if (interpreterDeclaration.emits() != sinkDeclaration.takes()) {
            throw new RuntimeException("Cannnot at all");
        }


//
//        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(TaskDefinition.class);
//        annotated.stream().map(k->k.getAnnotation(TaskDefinition.class)).collect(Collectors.groupingBy(k->k.ge));

        Type[] genericInterfaces = source.getGenericInterfaces();
        System.out.println("genericInterfaces = " + Arrays.toString(genericInterfaces));
        Source<U> source1 = source.getDeclaredConstructor().newInstance();
        Interpreter<U, V> interpreter1 = interpreter.getDeclaredConstructor().newInstance();
        Sink<V> sink1 = sink.getDeclaredConstructor().newInstance();
        return TaskExecutor.<U, V>builder().source(source1)
                                           .interpreter(interpreter1)
                                           .batchSize(11)
                                           .sink(sink1).build();
    }


    public static void main(String[] args) throws Exception {
        TaskExecutor<String, Integer> taskExecutor = TaskExecutorProvider.getTaskExecutor2();
        System.out.println("taskExecutor = " + taskExecutor);
        taskExecutor.initiate(new Task() {
            @Override
            public String name() {
                return "";
            }
        }, t -> {
        }, () -> false);
    }
}

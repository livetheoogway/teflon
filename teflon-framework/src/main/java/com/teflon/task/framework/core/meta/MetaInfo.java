package com.teflon.task.core.core;

import com.teflon.task.core.Interpreter;
import com.teflon.task.core.Sink;
import com.teflon.task.core.Source;
import lombok.Builder;
import lombok.Data;

/**
 * @author tushar.naik
 * @version 1.0  16/09/17 - 1:47 AM
 */
@Data
@Builder
public class MetaInfo {
    private InstanceFactory<? extends Source> sourceInstanceFactory;
    private InstanceFactory<? extends Interpreter> interpreterInstanceFactory;
    private InstanceFactory<? extends Sink> sinkInstanceFactory;
}

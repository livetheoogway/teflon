package com.teflon.task.framework.core.meta;

import com.teflon.task.framework.core.Interpreter;
import com.teflon.task.framework.core.Sink;
import com.teflon.task.framework.core.Source;
import com.teflon.task.framework.factory.InstanceFactory;
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
    private int batchSize;
}

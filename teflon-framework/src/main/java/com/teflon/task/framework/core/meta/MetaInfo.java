package com.teflon.task.framework.core.meta;

import com.teflon.task.framework.core.Interpreter;
import com.teflon.task.framework.core.Sink;
import com.teflon.task.framework.core.Source;
import com.teflon.task.framework.factory.InstanceFactory;
import lombok.Builder;
import lombok.Getter;

/**
 * A meta class that houses all required instance generating factories
 * and other meta properties
 *
 * @author tushar.naik
 * @version 1.0  16/09/17 - 1:47 AM
 */
@Builder
@Getter
public class MetaInfo {
    /* source object provider */
    private InstanceFactory<? extends Source> sourceInstanceFactory;

    /* interpreter object provider */
    private InstanceFactory<? extends Interpreter> interpreterInstanceFactory;

    /* sink object provider */
    private InstanceFactory<? extends Sink> sinkInstanceFactory;

    /* batch size for task execution */
    private int batchSize;

    @Override
    public String toString() {
        return "MetaInfo[" + "source:" + sourceInstanceFactory +
                ", interpreter:" + interpreterInstanceFactory +
                ", sink:" + sinkInstanceFactory +
                ", batchSize:" + batchSize +
                ']';
    }
}

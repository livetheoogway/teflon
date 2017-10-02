package com.teflon.task.framework.error;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author tushar.naik
 * @version 1.0  02/10/17 - 10:20 PM
 */
public class TeflonErrorTest {
    @Test
    public void testException() throws Exception {
        TeflonError test_exception = TeflonError.propagate("test exception", new RuntimeException());
        Assert.assertEquals(test_exception.getErrorCode(), ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testException2() throws Exception {
        TeflonError test_exception = TeflonError.propagate(new RuntimeException());
        Assert.assertEquals(test_exception.getErrorCode(), ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void testException3() throws Exception {
        TeflonError test_exception = TeflonError.propagate(ErrorCode.INVALID_DECLARATION, "", new RuntimeException());
        Assert.assertEquals(test_exception.getErrorCode(), ErrorCode.INVALID_DECLARATION);
    }
}
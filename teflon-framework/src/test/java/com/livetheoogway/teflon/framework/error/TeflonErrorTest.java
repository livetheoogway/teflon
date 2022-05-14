package com.livetheoogway.teflon.framework.error;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tushar.naik
 * @version 1.0  02/10/17 - 10:20 PM
 */
class TeflonErrorTest {
    @Test
    void testException() {
        TeflonError test_exception = TeflonError.propagate("test exception", new RuntimeException());
        assertEquals(test_exception.getErrorCode(), ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testException2() {
        TeflonError test_exception = TeflonError.propagate(new RuntimeException());
        assertEquals(test_exception.getErrorCode(), ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @Test
    void testException3() {
        TeflonError test_exception = TeflonError.propagate(ErrorCode.INVALID_DECLARATION, "", new RuntimeException());
        assertEquals(test_exception.getErrorCode(), ErrorCode.INVALID_DECLARATION);
    }
}
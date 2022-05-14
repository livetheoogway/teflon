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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tushar.naik
 * @version 1.0  02/10/17 - 10:20 PM
 */
class TeflonErrorTest {
    @Test
    void testException() {
        TeflonError testException = TeflonError.propagate("test exception", new RuntimeException());
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, testException.getErrorCode());
    }

    @Test
    void testException2() {
        TeflonError teflonError = TeflonError.propagate(new RuntimeException());
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, teflonError.getErrorCode());
    }

    @Test
    void testException3() {
        TeflonError teflonError = TeflonError.propagate(ErrorCode.INVALID_DECLARATION, "", new RuntimeException());
        assertEquals(ErrorCode.INVALID_DECLARATION, teflonError.getErrorCode());
    }
}
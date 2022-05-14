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

import lombok.Getter;

/**
 * @author tushar.naik
 * @version 1.0  15/09/17 - 8:52 PM
 */
public class TeflonError extends RuntimeException {
    @Getter
    private final ErrorCode errorCode;

    public TeflonError(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public TeflonError(ErrorCode errorCode, String message, Throwable e) {
        super(message, e);
        this.errorCode = errorCode;
    }

    public TeflonError(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public TeflonError(ErrorCode errorCode, Throwable e) {
        super(e);
        this.errorCode = errorCode;
    }

    public static TeflonError propagate(Throwable e) {
        if (e instanceof TeflonError error) {
            return error;
        }
        return new TeflonError(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }

    public static TeflonError propagate(String message, Throwable e) {
        if (e instanceof TeflonError error) {
            return error;
        }
        return new TeflonError(ErrorCode.INTERNAL_SERVER_ERROR, message + " Error:" + e.getMessage(), e);
    }

    public static TeflonError propagate(ErrorCode errorCode, String message, Throwable e) {
        if (e instanceof TeflonError error) {
            return error;
        }
        return new TeflonError(errorCode, message + " Error:" + e.getMessage(), e);
    }
}

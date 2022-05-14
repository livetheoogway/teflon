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
        if (e instanceof TeflonError) {
            return (TeflonError) e;
        }
        return new TeflonError(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage(), e);
    }

    public static TeflonError propagate(String message, Throwable e) {
        if (e instanceof TeflonError) {
            return (TeflonError) e;
        }
        return new TeflonError(ErrorCode.INTERNAL_SERVER_ERROR, message + " Error:" + e.getMessage(), e);
    }

    public static TeflonError propagate(ErrorCode errorCode, String message, Throwable e) {
        if (e instanceof TeflonError) {
            return (TeflonError) e;
        }
        return new TeflonError(errorCode, message + " Error:" + e.getMessage(), e);
    }
}

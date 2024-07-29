package com.quocnguyen.koko.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private int errCode;

    public ApiException(int code, String message) {
        super(message);
        this.errCode = code;
    }

    public ApiException(ErrorCode errorCode) {
        super(errorCode.getDescription());
        this.errCode = errorCode.getCode();
    }

    public ApiException(ErrorCode errorCode, String message) {
        super(message);
        this.errCode = errorCode.getCode();
    }
}

package com.quocnguyen.koko.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EXISTED_USER_EMAIL(1001, "Email is used by another user in this system"),
    EXISTED_USERNAME(1002, "Username is used by another user in this system");

    private final int code;
    private final String description;

    private ErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}

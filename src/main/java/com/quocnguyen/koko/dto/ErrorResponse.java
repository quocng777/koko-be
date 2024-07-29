package com.quocnguyen.koko.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private long code;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }
}

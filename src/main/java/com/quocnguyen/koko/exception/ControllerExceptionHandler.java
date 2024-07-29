package com.quocnguyen.koko.exception;

import com.quocnguyen.koko.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handeApiException(ApiException ex, WebRequest request) {
        ErrorResponse msg = new ErrorResponse(ex.getErrCode(), ex.getMessage());

        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

}

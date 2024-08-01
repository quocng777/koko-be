package com.quocnguyen.koko.exception;

import com.quocnguyen.koko.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handeApiException(ApiException ex, WebRequest request) {
        ErrorResponse msg = new ErrorResponse(ex.getErrCode(), ex.getMessage());

        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthenticationException ex, WebRequest request) {
        ErrorResponse msg = new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());

        return new ResponseEntity<>(msg, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse msg = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());

        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

}

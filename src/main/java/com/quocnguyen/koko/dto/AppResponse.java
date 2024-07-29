package com.quocnguyen.koko.dto;


import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class AppResponse<T> {
    private long code;
    private String message;
    private T data;

    protected AppResponse() {
    }

    protected AppResponse(long code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * The function help to create a response entity
     *
     * @param data
     */
    public static <T> AppResponse<T> response(T data, HttpStatus status, String message) {
        return new AppResponse<>(status.value(), message, data);
    }

    /**
     * The function help to create a successful response entity
     *
     * @param data: data want to wrap inside the response
     * */
    public static <T> AppResponse<T> success(T data) {
        return new AppResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), data);
    }

    public static <T> AppResponse<T> created(T data) {
        return new AppResponse<>(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), data);
    }

    public static <T> AppResponse<T> success(T data, String message) {
        return response(data, HttpStatus.OK, message);
    }

    public static <T> AppResponse<T> created(T data, String message) {
        return response(data, HttpStatus.CREATED, message);
    }

}

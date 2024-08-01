package com.quocnguyen.koko.exception;

/**
 * @author Quoc Nguyen on {8/1/2024}
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}

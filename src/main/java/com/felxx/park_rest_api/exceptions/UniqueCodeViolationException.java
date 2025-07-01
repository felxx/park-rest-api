package com.felxx.park_rest_api.exceptions;

public class UniqueCodeViolationException extends RuntimeException {

    public UniqueCodeViolationException(String message) {
        super(message);
    }
}

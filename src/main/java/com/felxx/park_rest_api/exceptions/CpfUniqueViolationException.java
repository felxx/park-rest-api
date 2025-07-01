package com.felxx.park_rest_api.exceptions;

public class CpfUniqueViolationException extends RuntimeException {

    public CpfUniqueViolationException(String message) {
        super(message);
    }
}

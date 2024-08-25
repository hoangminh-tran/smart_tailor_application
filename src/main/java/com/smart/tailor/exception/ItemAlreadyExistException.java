package com.smart.tailor.exception;

public class ItemAlreadyExistException extends RuntimeException {
    public ItemAlreadyExistException(String message) {
        super(message);
    }
}

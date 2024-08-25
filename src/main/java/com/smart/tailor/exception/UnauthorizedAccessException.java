package com.smart.tailor.exception;

public class UnauthorizedAccessException extends RuntimeException{
    public UnauthorizedAccessException(String message){
        super(message);
    }
}

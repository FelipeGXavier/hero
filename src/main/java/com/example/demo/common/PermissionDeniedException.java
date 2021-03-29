package com.example.demo.common;

public class PermissionDeniedException extends RuntimeException{

    public PermissionDeniedException(String message) {
        super(message);
    }
}

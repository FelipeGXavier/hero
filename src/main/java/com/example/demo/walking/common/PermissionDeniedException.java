package com.example.demo.walking.common;

public class PermissionDeniedException extends RuntimeException{

    public PermissionDeniedException(String message) {
        super(message);
    }
}

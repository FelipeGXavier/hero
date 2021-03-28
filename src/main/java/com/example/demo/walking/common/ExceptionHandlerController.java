package com.example.demo.walking.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = {IllegalStateException.class, IllegalArgumentException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        return new ErrorResponse(ex.getMessage(), false);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handlePermissionDeniedException(PermissionDeniedException ex) {
        return new ErrorResponse(ex.getMessage(), false);
    }

    @AllArgsConstructor
    @Getter
    static class ErrorResponse {
        private final String message;
        private final boolean success;
    }
}

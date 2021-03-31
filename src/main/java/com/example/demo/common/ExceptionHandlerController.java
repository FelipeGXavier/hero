package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerController {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(value = {IllegalStateException.class, IllegalArgumentException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleRuntimeException(RuntimeException ex) {
        this.logger.warn("Runtime exception caught, exception=" + ex.getClass(), ex);
        return new ErrorResponse(ex.getMessage(), false);
    }

    @ExceptionHandler(PermissionDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handlePermissionDeniedException(PermissionDeniedException ex) {
        this.logger.warn("Permission denied exception caught, exception=" + ex.getClass(), ex);
        return new ErrorResponse(ex.getMessage(), false);
    }

    @AllArgsConstructor
    @Getter
    static class ErrorResponse {
        private final String message;
        private final boolean success;
    }
}

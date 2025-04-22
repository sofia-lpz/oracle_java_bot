package com.springboot.MyTodoList.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            ex.getMessage(),
            ex.getClass().getName(),
            ex.getStackTrace()
        );
        return ResponseEntity.internalServerError().body(error);
    }

    public static class ErrorResponse {
        private String message;
        private String type;
        private StackTraceElement[] stackTrace;

        public ErrorResponse(String message, String type, StackTraceElement[] stackTrace) {
            this.message = message;
            this.type = type;
            this.stackTrace = stackTrace;
        }

        // Getters and setters
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public StackTraceElement[] getStackTrace() { return stackTrace; }
        public void setStackTrace(StackTraceElement[] stackTrace) { this.stackTrace = stackTrace; }
    }
}
package com.example.question_bank.exception;

import com.example.question_bank.exception.BusinessException;
import com.example.question_bank.exception.SettingsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(SettingsException.class)
    public ResponseEntity<ErrorResponse> handleSettingsException(SettingsException e) {
        ErrorResponse error = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse error = new ErrorResponse("服务器内部错误");
        return ResponseEntity.internalServerError().body(error);
    }

    private static class ErrorResponse {
        private final String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
} 
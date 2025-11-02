package com.example.crypto_trading.exception;

import com.example.crypto_trading.dto.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGeneral(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorMessage> handleIllegalArgumentAndStateException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    private ResponseEntity<ErrorMessage> buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(LocalDateTime.now(), status.value(), status.getReasonPhrase(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, status);
    }

}

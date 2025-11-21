package com.securevault.exception;

import com.securevault.exception.custom.ConcurrentWalletUpdateException;
import com.securevault.exception.custom.InsufficientBalanceException;
import com.securevault.exception.custom.UserNotFoundException;
import com.securevault.exception.custom.WalletNotFoundException;
import com.securevault.exception.payload.ExceptionMessage;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Hidden
@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(ConcurrentWalletUpdateException.class)
    public ResponseEntity<ExceptionMessage> handleConcurrentWalletUpdateException(final ConcurrentWalletUpdateException ex) {
        log.error("Unexpected Exception Occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionMessage.builder()
                        .msg(ex.getMessage())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ExceptionMessage> handleInsufficientBalanceException(final InsufficientBalanceException ex) {
        log.error("Unexpected Exception Occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionMessage.builder()
                        .msg(ex.getMessage())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleUserNotFoundException(final UserNotFoundException ex) {
        log.error("Unexpected Exception Occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ExceptionMessage.builder()
                        .msg(ex.getMessage())
                        .httpStatus(HttpStatus.NO_CONTENT)
                        .build());
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<ExceptionMessage> handleWalletNotFoundException(final WalletNotFoundException ex) {
        log.error("Unexpected Exception Occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(ExceptionMessage.builder()
                        .msg(ex.getMessage())
                        .httpStatus(HttpStatus.NO_CONTENT)
                        .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionMessage> handleRuntimeException(final Exception ex) {
        log.error("Unexpected Exception Occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionMessage.builder()
                        .msg(ex.getMessage())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

}
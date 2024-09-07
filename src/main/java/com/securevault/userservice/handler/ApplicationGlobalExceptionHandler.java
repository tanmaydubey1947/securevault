package com.securevault.userservice.handler;

import com.securevault.userservice.dto.ErrorDetails;
import com.securevault.userservice.exception.InvalidUserInformation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationGlobalExceptionHandler {

    @ExceptionHandler(InvalidUserInformation.class)
    public ResponseEntity<?> handleInvalidUserInformation(InvalidUserInformation invalidUserInformation) {
        ErrorDetails error = new ErrorDetails();
        error.setErrorCode(403);
        error.setErrorMessage(invalidUserInformation.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }
}

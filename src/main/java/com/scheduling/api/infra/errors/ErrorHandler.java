package com.scheduling.api.infra.errors;

import com.scheduling.api.domain.exceptions.DomainException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(HttpBaseException.class)
    public ResponseEntity<String> httpBaseException(HttpBaseException error) {
        return ResponseEntity
                .status(error.getHttpStatusCode())
                .body(error.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<String> domainErrorHandler(DomainException error){
        return ResponseEntity
                .badRequest()
                .body(error.getMessage());
    }

}

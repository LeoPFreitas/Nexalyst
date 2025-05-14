package com.nexalyst.apps.backend_core_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateOrganizationNameException.class)
    public ProblemDetail handleDuplicateOrganizationName(DuplicateOrganizationNameException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Duplicate Organization Name");
        problem.setType(java.net.URI.create("/errors/duplicate-organization"));
        problem.setProperty("timestamp", System.currentTimeMillis());
        return problem;
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleGenericException(RuntimeException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        problem.setTitle("Internal Server Error");
        problem.setType(java.net.URI.create("/errors/internal-server-error"));
        problem.setProperty("timestamp", System.currentTimeMillis());
        return problem;
    }
}
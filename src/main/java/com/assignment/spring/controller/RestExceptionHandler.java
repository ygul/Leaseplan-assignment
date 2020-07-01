package com.assignment.spring.controller;

import com.assignment.spring.domain.ErrorMessage;
import com.assignment.spring.exception.CityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CityNotFoundException.class})
    protected ResponseEntity<Object> cityNotFound(
            RuntimeException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage(ex.getMessage());
        return handleExceptionInternal(ex, message,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        ErrorMessage message = new ErrorMessage("General error");
        return handleExceptionInternal(ex, message,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}


package com.esantamarina.assignmentscalableweb.controller;

import com.esantamarina.assignmentscalableweb.domain.CustomApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class ApiErrorHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ApiErrorHandler.class);

    @Override
    protected ResponseEntity handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("The API returned an error with status code {} while processing a request", BAD_REQUEST, ex);
        CustomApiException customError = new CustomApiException();
        customError.setStatus(BAD_REQUEST);
        customError.setMessage("Request body is invalid");
        customError.setDebugMessage(ex.getLocalizedMessage());
        return buildResponseEntity(customError);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity handleApiException(ApiException ex) {
        log.info("The API returned an error with status code {} while processing a request", ex.getStatus(), ex);
        CustomApiException customError = new CustomApiException();
        customError.setMessage(ex.getMessage());
        customError.setStatus(ex.getStatus());
        return buildResponseEntity(customError);
    }

    private ResponseEntity<CustomApiException> buildResponseEntity(CustomApiException customError) {
        return ResponseEntity
                .status(customError.getStatus())
                .body(customError);
    }
}

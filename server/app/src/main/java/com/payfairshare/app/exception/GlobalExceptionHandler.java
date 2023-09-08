package com.payfairshare.app.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<CustomErrorResponse> handleResponseStatusException(ResponseStatusException ex) {
        CustomErrorResponse customErrorResponse = new CustomErrorResponse(ex.getRawStatusCode(), ex.getReason(), "/");
        return new ResponseEntity<>(customErrorResponse, ex.getStatus());
    }
}

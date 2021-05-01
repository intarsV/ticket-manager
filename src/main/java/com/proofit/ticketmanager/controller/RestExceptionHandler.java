package com.proofit.ticketmanager.controller;

import com.proofit.ticketmanager.domain.ExceptionResponse;
import com.proofit.ticketmanager.exception.TicketManagerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.proofit.ticketmanager.service.util.Constants.VARIABLES_ARE_NULL_CHECK_CLIENT_RESPONSES;
import static com.proofit.ticketmanager.service.util.Constants.ERROR_GETTING_DATA_FROM_CLIENT;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(TicketManagerException.class)
    protected ResponseEntity<Object> handleInternalErrors(TicketManagerException exception) {
        switch (exception.getMessage()) {
            case ERROR_GETTING_DATA_FROM_CLIENT:
            case VARIABLES_ARE_NULL_CHECK_CLIENT_RESPONSES:
                return new ResponseEntity<>(new ExceptionResponse(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
            default:
                return new ResponseEntity<>(new ExceptionResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        String.format("%s field validation failed", Objects.requireNonNull(e.getFieldError()).getField())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<Object> handleValidationExceptions(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(
                new ExceptionResponse("JSON parsing failed"), HttpStatus.BAD_REQUEST);
    }
}





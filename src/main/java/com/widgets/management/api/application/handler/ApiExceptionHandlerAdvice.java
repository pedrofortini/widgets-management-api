package com.widgets.management.api.application.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.widgets.management.api.application.exception.InvalidRequestException;
import com.widgets.management.api.application.exception.ResourceNotFoundException;
import com.widgets.management.api.application.exception.WidgetPersistenceException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@ControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class ApiExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception exception) {

        Gson gsonBuilder = new GsonBuilder().create();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(gsonBuilder.toJson(exception.getMessage()));
    }

    @ExceptionHandler(WidgetPersistenceException.class)
    public ResponseEntity<?> handlePersistenceException(WidgetPersistenceException exception) {

        Gson gsonBuilder = new GsonBuilder().create();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(gsonBuilder.toJson(exception.getMessage()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException exception) {

        Gson gsonBuilder = new GsonBuilder().create();

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(gsonBuilder.toJson(exception.getMessage()));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleInvalidRequestException(InvalidRequestException exception) {

        Gson gsonBuilder = new GsonBuilder().create();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(gsonBuilder.toJson(exception.getMessage()));
    }
}

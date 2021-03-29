package com.widgets.management.api.application.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.widgets.management.api.application.exception.InvalidRequestException;
import com.widgets.management.api.application.exception.ResourceNotFoundException;
import com.widgets.management.api.application.exception.WidgetPersistenceException;
import org.assertj.core.api.SoftAssertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;

public class ApiExceptionHandlerAdviceTest {

    private ApiExceptionHandlerAdvice advice;
    private Gson gsonBuilder;

    @Before
    public void setUp() {

        this.advice = new ApiExceptionHandlerAdvice();
        this.gsonBuilder = new GsonBuilder().create();
    }

    @Test
    public void shouldReturn_InternalServerErrorResponseEntity_WhenHandlingGenericException() {

        Exception e = new Exception("test message");
        ResponseEntity responseEntity = this.advice.handleGenericException(e);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        softly.assertThat(responseEntity.getBody()).isEqualTo(gsonBuilder.toJson("test message"));

        softly.assertAll();
    }

    @Test
    public void shouldReturn_InternalServerErrorResponseEntity_WhenHandlingPersistenceException() {

        WidgetPersistenceException e = new WidgetPersistenceException("test message");
        ResponseEntity responseEntity = this.advice.handlePersistenceException(e);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        softly.assertThat(responseEntity.getBody()).isEqualTo(gsonBuilder.toJson("test message"));

        softly.assertAll();
    }

    @Test
    public void shouldReturn_NotFoundResponseEntity_WhenHandlingResourceNotFoundException() {

        ResourceNotFoundException e = new ResourceNotFoundException("test message");
        ResponseEntity responseEntity = this.advice.handleResourceNotFoundException(e);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        softly.assertThat(responseEntity.getBody()).isEqualTo(gsonBuilder.toJson("test message"));

        softly.assertAll();
    }

    @Test
    public void shouldReturn_BadRequestResponseEntity_WhenHandlingInvalidRequestException() {

        InvalidRequestException e = new InvalidRequestException("test message");
        ResponseEntity responseEntity = this.advice.handleInvalidRequestException(e);

        SoftAssertions softly = new SoftAssertions();

        softly.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        softly.assertThat(responseEntity.getBody()).isEqualTo(gsonBuilder.toJson("test message"));

        softly.assertAll();
    }
}
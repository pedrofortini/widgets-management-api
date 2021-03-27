package com.machine.monitor.api.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends RuntimeException{

    private static final long serialVersionUID = 9169846267940483184L;

    public UnprocessableEntityException(String message) {
        super(message);
    }
}

package com.machine.monitor.api.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 4395748355543218740L;

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
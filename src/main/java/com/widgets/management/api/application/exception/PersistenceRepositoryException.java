package com.widgets.management.api.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PersistenceRepositoryException extends RuntimeException {

    private static final long serialVersionUID = -8130862747052025192L;

    public PersistenceRepositoryException(String message) {
        super(message);
    }
}

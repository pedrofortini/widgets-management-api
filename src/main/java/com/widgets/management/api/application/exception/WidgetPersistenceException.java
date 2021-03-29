package com.widgets.management.api.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class WidgetPersistenceException extends RuntimeException {

    private static final long serialVersionUID = -6929308905129344158L;

    public WidgetPersistenceException(String message) {
        super(message);
    }
}

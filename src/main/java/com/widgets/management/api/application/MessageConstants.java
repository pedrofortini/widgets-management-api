package com.widgets.management.api.application;

public interface MessageConstants {

    String MESSAGE_WIDGET_NOT_FOUND = "Couldn't find Widget with id %s";
    String MESSAGE_INVALID_GET_REQUEST = "Invalid Request. Current Page must be at least 0. Page size must be between 1 and 500";
    String MESSAGE_ERROR_DELETING_WIDGET_DATA = "An internal error ocurred while deleting Widget data";
    String MESSAGE_ERROR_PERSISTING_WIDGET_DATA = "An internal error ocurred while persisting Widget, invalid data provided";
    String MESSAGE_ERROR_PERSISTENCE_LAYER = "Internal error while persisting Widget entity";
    String LOG_MESSAGE_ERROR_REPOSITORY = "An error ocurred while trying to persist Widget. Id: %s; X: %s; Y: %s; Z-index: %s; Width: %s; Height: %s; LastModificationDate: %s";
}

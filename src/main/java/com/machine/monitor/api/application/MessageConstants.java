package com.machine.monitor.api.application;

public interface MessageConstants {

    String MESSAGE_USER_NOT_FOUND = "Couldn't find User with login %s";
    String MESSAGE_MACHINE_NOT_FOUND = "Couldn't find Machine with id %s";
    String MESSAGE_REVOKE_ADMIN_USER_ACESS = "Attempt to revoke admin user of a machine. User login: %s, Machine name: %s";
    String MESSAGE_ERROR_PERSISTING_MACHINE = "An error ocurred while persisting Machine %s";
    String MESSAGE_ERROR_PERSISTING_USER = "An error ocurred while persisting User %s";
    String MESSAGE_ERROR_PERSISTING_USER_ACESS = "An error ocurred while persisting the acess o User %s on Machine %s";
    String MESSAGE_ERROR_PERSISTING_MACHINE_EVENT_LOG = "An error ocurred while persisting event log for Machine %s";
}

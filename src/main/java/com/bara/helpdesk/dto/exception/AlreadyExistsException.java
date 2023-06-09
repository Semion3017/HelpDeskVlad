package com.bara.helpdesk.dto.exception;

public class AlreadyExistsException extends BaseException {

    public static final String ALREADY_EXISTS = "Already exists";

    public AlreadyExistsException() {
        super(ALREADY_EXISTS);
    }

    public AlreadyExistsException(String message) {
        super(message);
    }
}

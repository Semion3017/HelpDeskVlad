package com.bara.helpdesk.dto.exception;

public class IllegalActionException extends BaseException {
    public static final String ILLEGAL_ACTION = "You do not have permissions for this action";

    public IllegalActionException() {
        super(ILLEGAL_ACTION);
    }

    public IllegalActionException(String message) {
        super(message);
    }
}

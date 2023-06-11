package com.bara.helpdesk.dto.exception;

public class IllegalStateChangeException extends BaseException{
    public static final String ILLEGAL_STATE_CHANGE = "You do not have permissions for this action";

    public IllegalStateChangeException() {
        super(ILLEGAL_STATE_CHANGE);
    }

    public IllegalStateChangeException(String message) {
        super(message);
    }
}

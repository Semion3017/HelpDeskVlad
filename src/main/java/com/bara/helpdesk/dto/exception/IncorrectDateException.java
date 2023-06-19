package com.bara.helpdesk.dto.exception;

public class IncorrectDateException extends BaseException{
    public static final String INCORRECT_DATE = "Incorrect date";

    public IncorrectDateException() {
        super(INCORRECT_DATE);
    }

    public IncorrectDateException(String message) {
        super(message);
    }
}

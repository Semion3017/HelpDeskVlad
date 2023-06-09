package com.bara.helpdesk.dto.exception;

public class UserNotFoundException extends BaseException{
    public static final String USER_NOT_FOUND = "User not found";

    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}

package com.bara.helpdesk.dto.exception;

public class PasswordRequirementsException extends BaseException{
    public static final String PASSWORD_REQUIREMENTS_NOT_MET =
            "Password must contain atl least: 8 characters. \n" +
                    "At least one uppercase letter\n" +
                    "At least one lowercase letter\n" +
                    "At least one number\n" +
                    "At least one special character (!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ )";

    public PasswordRequirementsException() {
        super(PASSWORD_REQUIREMENTS_NOT_MET);
    }

    public PasswordRequirementsException(String message) {
        super(message);
    }
}

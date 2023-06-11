package com.bara.helpdesk.dto.exception;

public class PasswordRequirementsException extends BaseException{
    public static final String PASSWORD_REQUIREMENTS_NOT_MET =
            "Password must contain atl least: 8 characters." +
                    "At least one uppercase letter." +
                    "At least one lowercase letter." +
                    "At least one number." +
                    "At least one special character (!  \"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~ )";

    public PasswordRequirementsException() {
        super(PASSWORD_REQUIREMENTS_NOT_MET);
    }

    public PasswordRequirementsException(String message) {
        super(message);
    }
}

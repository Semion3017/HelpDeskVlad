package com.bara.helpdesk.dto.exception;

public class EmailRequirementsException extends BaseException{
    public static final String EMAIL_REQUIREMENTS_NOT_MET =
            "Enter a valid email";

    public EmailRequirementsException() {
        super(EMAIL_REQUIREMENTS_NOT_MET);
    }

    public EmailRequirementsException(String message) {
        super(message);
    }
}

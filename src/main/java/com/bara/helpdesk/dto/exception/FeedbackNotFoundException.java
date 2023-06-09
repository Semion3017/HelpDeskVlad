package com.bara.helpdesk.dto.exception;


public class FeedbackNotFoundException extends BaseException {
    public static final String FEEDBACK_NOT_FOUND = "FEEDBACK not found";

    public FeedbackNotFoundException() {
        super(FEEDBACK_NOT_FOUND);
    }

    public FeedbackNotFoundException(String message) {
        super(message);
    }
}

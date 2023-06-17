package com.bara.helpdesk.dto.exception;

public class AttachmentNotFoundException extends BaseException{
    public static final String ATTACHMENT_NOT_FOUND = "Attachment not found";

    public AttachmentNotFoundException() {
        super(ATTACHMENT_NOT_FOUND);
    }

    public AttachmentNotFoundException(String message) {
        super(message);
    }
}

package com.bara.helpdesk.dto.exception;

public class IllegalFileExtensionException extends BaseException {
    public static final String ILLEGAL_FILE_EXTENSION =
            "The selected file type is not allowed. Please select a file of one of the following types: pdf, png, doc, docx, jpg, jpeg.";

    public IllegalFileExtensionException() {
        super(ILLEGAL_FILE_EXTENSION);
    }

    public IllegalFileExtensionException(String message) {
        super(message);
    }
}

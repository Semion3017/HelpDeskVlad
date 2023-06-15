package com.bara.helpdesk.dto.exception;

public class FileSizeException extends BaseException {
    public static final String ILLEGAL_FILE_SIZE =
            "File size mut be less than 50Mb";

    public FileSizeException() {
        super(ILLEGAL_FILE_SIZE);
    }

    public FileSizeException(String message) {
        super(message);
    }
}

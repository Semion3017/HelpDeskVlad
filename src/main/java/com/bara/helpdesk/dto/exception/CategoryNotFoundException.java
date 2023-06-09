package com.bara.helpdesk.dto.exception;

public class CategoryNotFoundException extends BaseException {

    public static final String CATEGORY_NOT_FOUND = "CATEGORY not found";

    public CategoryNotFoundException() {
        super(CATEGORY_NOT_FOUND);
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}

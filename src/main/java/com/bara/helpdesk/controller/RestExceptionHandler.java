package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(FeedbackNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFeedbackNotFoundException(FeedbackNotFoundException e, WebRequest request) {
        LOGGER.error(FeedbackNotFoundException.FEEDBACK_NOT_FOUND, e);
        ErrorResponse errorResponse = buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTicketNotFoundException(TicketNotFoundException e, WebRequest request) {
        LOGGER.error(TicketNotFoundException.TICKET_NOT_FOUND, e);
        ErrorResponse errorResponse = buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTicketNotFoundException(UserNotFoundException e, WebRequest request) {
        LOGGER.error(UserNotFoundException.USER_NOT_FOUND, e);
        ErrorResponse errorResponse = buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTicketNotFoundException(CategoryNotFoundException e, WebRequest request) {
        LOGGER.error(CategoryNotFoundException.CATEGORY_NOT_FOUND, e);
        ErrorResponse errorResponse = buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleTicketNotFoundException(AlreadyExistsException e, WebRequest request) {
        LOGGER.error(AlreadyExistsException.ALREADY_EXISTS, e);
        ErrorResponse errorResponse = buildErrorResponse(e.getMessage(), HttpStatus.CONFLICT, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PasswordRequirementsException.class)
    public ResponseEntity<ErrorResponse> handlePasswordRequirementsException(PasswordRequirementsException e, WebRequest request) {
        LOGGER.error(PasswordRequirementsException.PASSWORD_REQUIREMENTS_NOT_MET, e);
        ErrorResponse errorResponse = buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateChangeException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateChange(IllegalStateChangeException e, WebRequest request) {
        LOGGER.error(IllegalStateChangeException.ILLEGAL_STATE_CHANGE, e);
        ErrorResponse errorResponse = buildErrorResponse(
                IllegalStateChangeException.ILLEGAL_STATE_CHANGE,
                HttpStatus.FORBIDDEN,
                request
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(Exception e, WebRequest request) {
        if (e instanceof NullPointerException) {
            return new ResponseEntity<>(buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST, request), HttpStatus.BAD_REQUEST);
        }
        LOGGER.error(e.getMessage(), e);
        ErrorResponse errorResponse = buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse buildErrorResponse(String message, HttpStatus status, WebRequest request) {
        return new ErrorResponse(
                LocalDateTime.now().toString(),
                status.value(),
                message,
                ((ServletWebRequest) request).getRequest().getRequestURI());
    }


}

package com.bara.helpdesk.dto.exception;


public class TicketNotFoundException extends BaseException {
    public static final String TICKET_NOT_FOUND = "Ticket not found";

    public TicketNotFoundException() {
        super(TICKET_NOT_FOUND);
    }

    public TicketNotFoundException(String message) {
        super(message);
    }
}


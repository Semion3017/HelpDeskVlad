package com.bara.helpdesk.service;

import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.enums.State;
import org.springframework.scheduling.annotation.Async;

public interface MailService {
    @Async
    void sendFeedbackProvidedMessage(Ticket ticket);

    void sendTicketStateChangeMessage(Ticket ticket, State oldState);
}

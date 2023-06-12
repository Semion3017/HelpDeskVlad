package com.bara.helpdesk.service;

import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.enums.State;

public interface MailService {
    void sendTicketStateChangeMessage(Ticket ticket, State oldState);
}

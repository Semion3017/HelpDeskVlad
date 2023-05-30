package com.bara.helpdesk.service;

import com.bara.helpdesk.dto.HistoryOutputDto;
import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.enums.State;

import java.util.List;

public interface HistoryService {

    List<HistoryOutputDto> getByTicketId(Long ticketId);

    String logTicketCreation(Ticket ticket);

    String logTicketUpdate(Ticket ticket);

    String logStateChange(State oldState, Ticket ticket, Long userId);
}

package com.bara.helpdesk.service;

import com.bara.helpdesk.dto.HistoryOutputDto;
import com.bara.helpdesk.dto.PageOutputDto;
import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.enums.State;

public interface HistoryService {

    PageOutputDto<HistoryOutputDto> getByTicketId(Long ticketId, Integer page, Integer size);

    String logTicketCreation(Ticket ticket);

    String logTicketUpdate(Ticket ticket);

    String logFileAttached(Ticket ticket, String attachmentName);

    String logFileRemoved(Ticket ticket, String attachmentName);

    String logStateChange(State oldState, Ticket ticket, Long userId);
}

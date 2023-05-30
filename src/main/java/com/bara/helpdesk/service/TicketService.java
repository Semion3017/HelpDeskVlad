package com.bara.helpdesk.service;

import com.bara.helpdesk.dto.TicketCreateDto;
import com.bara.helpdesk.dto.TicketEditDto;
import com.bara.helpdesk.dto.TicketOutputDto;
import com.bara.helpdesk.dto.TicketStateChangeDto;

import java.util.List;

public interface TicketService {

    List<TicketOutputDto> getAllTickets();

    TicketOutputDto getById(Long id);

    TicketOutputDto createTicket(TicketCreateDto ticketDto, Long id);

    TicketOutputDto updateTicket(TicketEditDto dto);

    String changeTicketState(TicketStateChangeDto dto, Long userId);

    List<TicketOutputDto> getByUserId(Long userId);
}

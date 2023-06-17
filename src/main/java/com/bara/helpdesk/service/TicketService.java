package com.bara.helpdesk.service;

import com.bara.helpdesk.dto.*;
import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.security.CustomUserDetails;
import jakarta.transaction.Transactional;

import java.util.List;

public interface TicketService {

    List<TicketOutputDto> getAllTickets(CustomUserDetails userDetails);

    PageOutputDto<TicketOutputDto> getAllSortedTickets(SortTicketParametersDto params, CustomUserDetails userDetails);

    TicketOutputDto getDtoById(Long id);

    Ticket getById(Long id);

    TicketOutputDto createTicket(TicketCreateDto ticketDto, Long id);

    TicketOutputDto updateTicket(TicketEditDto dto);

    @Transactional
    TicketOutputDto changeTicketState(TicketStateChangeDto dto, CustomUserDetails userDetails);
}

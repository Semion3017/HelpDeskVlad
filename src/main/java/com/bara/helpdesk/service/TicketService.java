package com.bara.helpdesk.service;

import com.bara.helpdesk.dto.*;
import com.bara.helpdesk.security.CustomUserDetails;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TicketService {

    List<TicketOutputDto> getAllTickets(CustomUserDetails userDetails);

    Page<TicketOutputDto> getAllSortedTickets(SortTicketParametersDto params, CustomUserDetails userDetails);

    TicketOutputDto getById(Long id);

    TicketOutputDto createTicket(TicketCreateDto ticketDto, Long id);

    TicketOutputDto updateTicket(TicketEditDto dto);

    TicketOutputDto changeTicketState(TicketStateChangeDto dto, Long userId);

    List<TicketOutputDto> getByUserId(CustomUserDetails userDetails);
}

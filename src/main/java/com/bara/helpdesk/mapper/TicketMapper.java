package com.bara.helpdesk.mapper;

import com.bara.helpdesk.dto.CategoryDto;
import com.bara.helpdesk.dto.TicketCreateDto;
import com.bara.helpdesk.dto.TicketEditDto;
import com.bara.helpdesk.dto.TicketOutputDto;
import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.enums.State;
import com.bara.helpdesk.entity.enums.Urgency;

import java.time.LocalDate;


public class TicketMapper {

    public static TicketOutputDto ToDto(Ticket ticket) {
        return TicketOutputDto.builder()
                .id(ticket.getId())
                .name(ticket.getName())
                .description(ticket.getDescription())
                .createdOn(ticket.getCreatedOn())
                .urgency(ticket.getUrgency().name())
                .state(ticket.getState().name())
                .desiredDate(ticket.getDesiredResolutionDate())
                .category(new CategoryDto(ticket.getCategory().getId(), ticket.getCategory().getName()))
                .owner(UserMapper.toDto(ticket.getOwner()))
                .approver(UserMapper.toDto(ticket.getApprover()))
                .assignee(UserMapper.toDto(ticket.getAssignee()))
                .build();
    }

    public static Ticket toEntity(TicketCreateDto dto) {
        State state = State.valueOf(dto.getState());
        if (state != State.DRAFT & state != State.NEW) {
            state = State.DRAFT;
        }
        return Ticket.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .createdOn(LocalDate.now())
                .desiredResolutionDate(dto.getDesiredDate())
                .urgency(Urgency.valueOf(dto.getUrgency()))
                .urgencyNumber(Urgency.valueOf(dto.getUrgency()).ordinal())
                .state(state)
                .build();
    }
    public static Ticket toEntity(TicketEditDto dto) {
        State state = State.valueOf(dto.getState());
        if (State.valueOf(dto.getState()) != State.DRAFT & State.valueOf(dto.getState()) != State.NEW) {
            state = State.DRAFT;
        }
        return Ticket.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .desiredResolutionDate(dto.getDesiredDate())
                .urgency(Urgency.valueOf(dto.getUrgency()))
                .urgencyNumber(Urgency.valueOf(dto.getUrgency()).ordinal())
                .state(state)
                .build();
    }
}

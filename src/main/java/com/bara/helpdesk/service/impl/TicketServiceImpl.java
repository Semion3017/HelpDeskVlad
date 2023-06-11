package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.*;
import com.bara.helpdesk.dto.exception.CategoryNotFoundException;
import com.bara.helpdesk.dto.exception.IllegalStateChangeException;
import com.bara.helpdesk.dto.exception.TicketNotFoundException;
import com.bara.helpdesk.dto.exception.UserNotFoundException;
import com.bara.helpdesk.entity.Category;
import com.bara.helpdesk.entity.Comment;
import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.User;
import com.bara.helpdesk.entity.enums.Role;
import com.bara.helpdesk.entity.enums.State;
import com.bara.helpdesk.mapper.TicketMapper;
import com.bara.helpdesk.repository.CategoryRepository;
import com.bara.helpdesk.repository.CommentRepository;
import com.bara.helpdesk.repository.TicketRepository;
import com.bara.helpdesk.repository.UserRepository;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.HistoryService;
import com.bara.helpdesk.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final HistoryService historyService;

    @Override
    public List<TicketOutputDto> getAllTickets(CustomUserDetails userDetails) {
        return ticketRepository.findAll().stream().map(ticket -> {
            List<ActionDto> actions = getTicketActions(ticket, userDetails);
            TicketOutputDto dto = TicketMapper.ToDto(ticket);
            dto.setActions(actions);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<TicketOutputDto> getAllSortedTickets(int page, int size, String columnName, String direction) {
        Sort.Direction sortDirection = direction.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        return ticketRepository.findAll(PageRequest.of(page, size, Sort.by(sortDirection, columnName))).map(TicketMapper::ToDto);
    }

    @Override
    public TicketOutputDto getById(Long id) {
        return ticketRepository.findById(id).map(TicketMapper::ToDto).orElseThrow(() -> new TicketNotFoundException("Ticket with ID: " + id + " not found"));
    }

    @Override
    public TicketOutputDto createTicket(TicketCreateDto dto, Long ownerId) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID: " + dto.getCategoryId() + " not found"));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + ownerId + " not found"));
        Ticket ticket = TicketMapper.toEntity(dto);
        if (ticket.getState().equals(State.NEW)) {
            //TODO send an email to all MANAGERS
        }
        ticket.setCategory(category);
        ticket.setOwner(owner);
        Ticket createdTicket = ticketRepository.save(ticket);
        TicketOutputDto createdTicketDto = TicketMapper.ToDto(createdTicket);
        historyService.logTicketCreation(createdTicket);
        if (dto.getComment() == null) return createdTicketDto;
        commentRepository.save(
                Comment.builder()
                        .date(LocalDateTime.now())
                        .user(owner)
                        .text(dto.getComment())
                        .ticket(ticket)
                        .build()
        );

        return createdTicketDto;
    }

    @Override
    public TicketOutputDto updateTicket(TicketEditDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID: " + dto.getCategoryId() + " not found"));
        Ticket oldTicket = ticketRepository.findById(dto.getId())
                .orElseThrow(() -> new TicketNotFoundException("Ticket with ID: " + dto.getId() + " not found"));
        Ticket updatedTicket = TicketMapper.toEntity(dto);
        updatedTicket.setId(oldTicket.getId());
        updatedTicket.setOwner(oldTicket.getOwner());
        updatedTicket.setCreatedOn(oldTicket.getCreatedOn());
        updatedTicket.setCategory(category);
        historyService.logTicketUpdate(updatedTicket);
        return TicketMapper.ToDto(ticketRepository.save(updatedTicket));
    }

    @Override
    public String changeTicketState(TicketStateChangeDto dto, Long userId) {
        Ticket ticket = ticketRepository.findById(dto.getId())
                .orElseThrow(() -> new TicketNotFoundException("Ticket with ID: " + dto.getId() + " not found"));
        State oldState = ticket.getState();
        State newState = State.valueOf(dto.getState());
        User actor = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with " + userId + " not found"));
        processTicketStateChange(newState, actor, ticket);
        ticketRepository.save(ticket);
        return historyService.logStateChange(oldState, ticket, userId);
    }

    @Override
    public List<TicketOutputDto> getByUserId(CustomUserDetails userDetails) {
        return ticketRepository.findByUserId(userDetails.getId()).stream()
                .map(ticket -> {
                    List<ActionDto> actions = getTicketActions(ticket, userDetails);
                    TicketOutputDto dto = TicketMapper.ToDto(ticket);
                    dto.setActions(actions);
                    return dto;
                }).collect(Collectors.toList());
    }

    private void processTicketStateChange(State newState, User actor, Ticket ticket) {
        State oldState = ticket.getState();
        ticket.setState(newState);
        if ((newState == State.NEW | newState == State.CANCELED) && (oldState == State.DECLINED | oldState == State.DRAFT) && actor == ticket.getOwner()) {
            //mail all managers
        } else if (newState == State.APPROVED && oldState == State.NEW && Role.MANAGER.equals(actor.getRole())) {
            ticket.setApprover(actor);
            //mail owner and all engineers
        } else if (newState == State.DECLINED && oldState == State.NEW && Role.MANAGER.equals(actor.getRole())) {
            //mail owner
        } else if (newState == State.CANCELED && oldState == State.NEW && Role.MANAGER.equals(actor.getRole())) {
            //mail owner
        } else if (newState == State.CANCELED && oldState == State.APPROVED && Role.ENGINEER.equals(actor.getRole())) {
            //mail owner and approver
        } else if (newState == State.IN_PROGRESS && oldState == State.APPROVED && Role.ENGINEER.equals(actor.getRole())) {
            ticket.setAssignee(actor);
            //mail owner
        } else if (newState == State.DONE && oldState == State.IN_PROGRESS && Role.ENGINEER.equals(actor.getRole())) {
            //mail owner
        } else {
            throw new IllegalStateChangeException();
        }
    }

    private List<ActionDto> getTicketActions(Ticket ticket, CustomUserDetails userDetails) {
        if (ticket.getState() == State.DRAFT && ticket.getOwner().getId() == userDetails.getId()) {
            return Arrays.asList(new ActionDto(State.NEW), new ActionDto(State.CANCELED));

        }
        if (ticket.getState() == State.NEW && userDetails.getRole() == Role.MANAGER && ticket.getOwner().getId() != userDetails.getId()) {
            return Arrays.asList(new ActionDto(State.APPROVED), new ActionDto(State.CANCELED), new ActionDto(State.DECLINED));

        }
        if (ticket.getState() == State.APPROVED && userDetails.getRole() == Role.ENGINEER) {
            return Arrays.asList(new ActionDto(State.IN_PROGRESS), new ActionDto(State.CANCELED));

        }
        if (ticket.getState() == State.DECLINED && ticket.getOwner().getId() == userDetails.getId()) {
            return Arrays.asList(new ActionDto(State.NEW), new ActionDto(State.CANCELED));
            //Submit
            //mail to all managers
            //Cancel
        }
        if (ticket.getState() == State.IN_PROGRESS && ticket.getAssignee().getId() == userDetails.getId()) {
            return Arrays.asList(new ActionDto(State.DONE));
            //Done
            //Mail to owner
            //also when owner left feedback, mail to assignee
        }
        return Arrays.asList();
    }

}

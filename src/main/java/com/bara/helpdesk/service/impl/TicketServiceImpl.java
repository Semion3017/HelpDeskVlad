package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.*;
import com.bara.helpdesk.dto.exception.IllegalActionException;
import com.bara.helpdesk.dto.exception.IncorrectDateException;
import com.bara.helpdesk.dto.exception.TicketNotFoundException;
import com.bara.helpdesk.entity.*;
import com.bara.helpdesk.entity.enums.Role;
import com.bara.helpdesk.entity.enums.State;
import com.bara.helpdesk.mapper.TicketMapper;
import com.bara.helpdesk.repository.CommentRepository;
import com.bara.helpdesk.repository.TicketRepository;
import com.bara.helpdesk.repository.specification.ticket.TicketSpecifications;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final CommentRepository commentRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final HistoryService historyService;
    private final MailService mailService;


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
    public PageOutputDto<TicketOutputDto> getAllSortedTickets(SortTicketParametersDto params, CustomUserDetails userDetails) {
        User user = userService.getById(userDetails.getId());
        Sort.Direction sortDirection = params.getDirection().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort.Order paramsOrder = new Sort.Order(sortDirection, params.getColumnName());
        Sort.Order desiredDateOrder = new Sort.Order(Sort.Direction.ASC, Ticket_.DESIRED_RESOLUTION_DATE);
        Integer count = ticketRepository
                .findAll(TicketSpecifications.filterAllByUser(user, params.getIsAll()).and(TicketSpecifications.ticketFieldsLikeKeyword(params.getKeyword())))
                .size();
        Page<TicketOutputDto> page = ticketRepository.findAll(
                        TicketSpecifications.filterAllByUser(user, params.getIsAll()).and(TicketSpecifications.ticketFieldsLikeKeyword(params.getKeyword())),
                        PageRequest.of(params.getPage(), params.getSize(), Sort.by(paramsOrder, desiredDateOrder)))
                .map(ticket -> {
                    List<ActionDto> actions = getTicketActions(ticket, userDetails);
                    TicketOutputDto dto = TicketMapper.ToDto(ticket);
                    dto.setActions(actions);
                    return dto;
                });
        return new PageOutputDto<>(page.getContent(), count);
    }

    @Override
    public TicketOutputDto getDtoById(Long id) {
        return TicketMapper.ToDto(getById(id));
    }

    @Override
    public Ticket getById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException("Ticket with ID: " + id + " not found"));
    }

    @Override
    @Transactional
    public TicketOutputDto createTicket(TicketCreateDto dto, Long ownerId) {
        Category category = categoryService.getById(dto.getCategoryId());
        User owner = userService.getById(ownerId);
        Ticket ticket = TicketMapper.toEntity(dto);
        ticket.setCategory(category);
        ticket.setOwner(owner);
        if (LocalDate.now().isAfter(ticket.getDesiredResolutionDate())){
            throw new IncorrectDateException();
        }
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
        Ticket oldTicket = getById(dto.getId());
        if (State.DRAFT != oldTicket.getState()) {
            throw new IllegalActionException();
        }
        Category category = categoryService.getById(dto.getCategoryId());
        Ticket updatedTicket = TicketMapper.toEntity(dto);
        updatedTicket.setId(oldTicket.getId());
        updatedTicket.setOwner(oldTicket.getOwner());
        updatedTicket.setCreatedOn(oldTicket.getCreatedOn());
        updatedTicket.setCategory(category);
        updatedTicket.setAttachments(oldTicket.getAttachments());
        historyService.logTicketUpdate(updatedTicket);
        return TicketMapper.ToDto(ticketRepository.save(updatedTicket));
    }

    @Override
    @Transactional
    public TicketOutputDto changeTicketState(TicketStateChangeDto dto, CustomUserDetails userDetails) {
        Ticket ticket = getById(dto.getId());
        State oldState = ticket.getState();
        State newState = State.valueOf(dto.getState());
        setTicketState(newState, userDetails, ticket);
        Ticket savedTicket = ticketRepository.save(ticket);
        historyService.logStateChange(oldState, ticket, userDetails.getId());
        mailService.sendTicketStateChangeMessage(ticket, oldState);
        return TicketMapper.ToDto(savedTicket);
    }

    private void setTicketState(State newState, CustomUserDetails actor, Ticket ticket) throws IllegalActionException {
        State oldState = ticket.getState();
        ticket.setState(newState);
        if ((State.DRAFT.equals(newState) | State.NEW.equals(newState) | State.CANCELED.equals(newState))
                && (State.DECLINED.equals(oldState) | State.DRAFT.equals(oldState)) && isOwner(ticket, actor)) {

        } else if (State.DECLINED.equals(newState) && State.NEW.equals(oldState) && Role.MANAGER.equals(actor.getRole())) {

        } else if (State.CANCELED.equals(newState) && State.NEW.equals(oldState) && Role.MANAGER.equals(actor.getRole())) {

        } else if (State.CANCELED.equals(newState) && State.APPROVED.equals(oldState) && Role.ENGINEER.equals(actor.getRole())) {

        } else if (State.APPROVED.equals(newState) && State.NEW.equals(oldState) && Role.MANAGER.equals(actor.getRole())) {
            ticket.setApprover(userService.getById(actor.getId()));
        } else if (State.IN_PROGRESS.equals(newState) && State.APPROVED.equals(oldState) && Role.ENGINEER.equals(actor.getRole())) {
            ticket.setAssignee(userService.getById(actor.getId()));
        } else if (!State.DONE.equals(newState) || !State.IN_PROGRESS.equals(oldState) || !Role.ENGINEER.equals(actor.getRole())) {
            throw new IllegalActionException();
        }
    }

    private List<ActionDto> getTicketActions(Ticket ticket, CustomUserDetails userDetails) {
        if (State.DRAFT.equals(ticket.getState()) && isOwner(ticket, userDetails)) {
            return List.of(new ActionDto(State.NEW), new ActionDto(State.CANCELED));
        }
        if (State.NEW.equals(ticket.getState()) && userDetails.getRole() == Role.MANAGER && !isOwner(ticket, userDetails)) {
            return List.of(new ActionDto(State.APPROVED), new ActionDto(State.CANCELED), new ActionDto(State.DECLINED));
        }
        if (State.APPROVED.equals(ticket.getState()) && userDetails.getRole() == Role.ENGINEER) {
            return List.of(new ActionDto(State.IN_PROGRESS), new ActionDto(State.CANCELED));
        }
        if (State.DECLINED.equals(ticket.getState()) && isOwner(ticket, userDetails)) {
            return List.of(new ActionDto(State.DRAFT), new ActionDto(State.CANCELED));
        }
        if (State.IN_PROGRESS.equals(ticket.getState()) && userDetails.getId().equals(ticket.getAssignee().getId())) {
            return List.of(new ActionDto(State.DONE));
        }
        return List.of();
    }

    private Boolean isOwner(Ticket ticket, CustomUserDetails userDetails) {
        return userDetails.getId().equals(ticket.getOwner().getId());
    }
}

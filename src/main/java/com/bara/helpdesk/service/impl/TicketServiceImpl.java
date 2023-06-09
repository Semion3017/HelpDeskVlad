package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.*;
import com.bara.helpdesk.dto.exception.CategoryNotFoundException;
import com.bara.helpdesk.dto.exception.TicketNotFoundException;
import com.bara.helpdesk.dto.exception.UserNotFoundException;
import com.bara.helpdesk.entity.Category;
import com.bara.helpdesk.entity.Comment;
import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.User;
import com.bara.helpdesk.entity.enums.State;
import com.bara.helpdesk.mapper.TicketMapper;
import com.bara.helpdesk.repository.CategoryRepository;
import com.bara.helpdesk.repository.CommentRepository;
import com.bara.helpdesk.repository.TicketRepository;
import com.bara.helpdesk.repository.UserRepository;
import com.bara.helpdesk.service.HistoryService;
import com.bara.helpdesk.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
    public List<TicketOutputDto> getAllTickets() {
        return ticketRepository.findAll().stream().map(TicketMapper::ToDto).collect(Collectors.toList());
    }

    @Override
    public Page<TicketOutputDto> getAllSortedTickets(int page, int size, String columnName, String direction){
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
                .orElseThrow(() -> new  CategoryNotFoundException("Category with ID: " + dto.getCategoryId() + " not found"));
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + ownerId + " not found"));
        Ticket ticket = TicketMapper.toEntity(dto);
        if (ticket.getState().equals(State.NEW)){
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
                .orElseThrow(() -> new  CategoryNotFoundException("Category with ID: " + dto.getCategoryId() + " not found"));
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
        ticket.setState(State.valueOf(dto.getState()));
        ticketRepository.save(ticket);
        return historyService.logStateChange(oldState, ticket, userId);
    }

    @Override
    public List<TicketOutputDto> getByUserId(Long userId) {
        return ticketRepository.findByUserId(userId).stream().map(TicketMapper::ToDto).collect(Collectors.toList());
    }
}

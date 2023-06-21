package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.*;
import com.bara.helpdesk.dto.exception.IllegalActionException;
import com.bara.helpdesk.dto.exception.IncorrectDateException;
import com.bara.helpdesk.dto.exception.TicketNotFoundException;
import com.bara.helpdesk.entity.*;
import com.bara.helpdesk.entity.enums.Role;
import com.bara.helpdesk.entity.enums.State;
import com.bara.helpdesk.entity.enums.Urgency;
import com.bara.helpdesk.mapper.TicketMapper;
import com.bara.helpdesk.repository.CommentRepository;
import com.bara.helpdesk.repository.TicketRepository;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketServiceImplTests {

    private final TicketRepository ticketRepository = mock(TicketRepository.class);
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final CategoryService categoryService = mock(CategoryService.class);
    private final UserService userService = mock(UserService.class);
    private final HistoryService historyService = mock(HistoryService.class);
    private final MailService mailService = mock(MailService.class);

    private final TicketService ticketService = new TicketServiceImpl(
            ticketRepository,
            commentRepository,
            categoryService,
            userService,
            historyService,
            mailService);

    User owner = User.builder().id(1L).email("owner@email.com").role(Role.OWNER).password("P@ssword1").build();
    User manager = User.builder().id(2L).email("manager@email.com").role(Role.MANAGER).password("P@ssword1").build();
    User assignee = User.builder().id(3L).email("engineer@email.com").role(Role.ENGINEER).password("P@ssword1").build();
    Category category = Category.builder().id(1L).name("name").build();
    Ticket ticket = Ticket.builder()
            .id(1L)
            .name("name")
            .description("description")
            .createdOn(LocalDate.now())
            .desiredResolutionDate(LocalDate.now().plusDays(1))
            .owner(owner)
            .state(State.NEW)
            .urgency(Urgency.CRITICAL)
            .urgencyNumber(1)
            .category(category)
            .build();
    Comment comment = Comment.builder()
            .id(1L)
            .user(owner)
            .text("text")
            .date(LocalDateTime.now())
            .ticket(ticket)
            .build();

    @Test
    void shouldReturnPageTicketOutputDto() {
        CustomUserDetails userDetails = new CustomUserDetails(owner);
        List<Ticket> ticketList = new ArrayList<Ticket>();
        ticketList.add(ticket);
        SortTicketParametersDto params = SortTicketParametersDto.builder()
                .page(0)
                .size(1)
                .isAll(true)
                .keyword("")
                .direction("asc")
                .columnName("id")
                .build();
        List<ActionDto> actions = List.of();
        Sort.Direction sortDirection = params.getDirection().equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort.Order paramsOrder = new Sort.Order(sortDirection, params.getColumnName());
        Sort.Order desiredDateOrder = new Sort.Order(Sort.Direction.ASC, Ticket_.DESIRED_RESOLUTION_DATE);
        Pageable pageable = PageRequest.of(params.getPage(), params.getSize(), Sort.by(paramsOrder, desiredDateOrder));
        Page<Ticket> page = new PageImpl<>(ticketList, pageable, 1);
        Page<TicketOutputDto> dtoPage = new PageImpl<>(ticketList.stream().map(ticketFromList -> {
            TicketOutputDto dto = TicketMapper.ToDto(ticketFromList);
            dto.setActions(actions);
            return dto;
        }).toList(), pageable, 1);
        when(ticketRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        Assertions.assertIterableEquals(dtoPage, ticketService.getAllSortedTickets(params, userDetails));
    }

    @Test
    void shouldReturnDtoById() {
        TicketOutputDto dto = TicketMapper.ToDto(ticket);
        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.ofNullable(ticket));
        Assertions.assertEquals(dto, ticketService.getDtoById(ticket.getId()));
    }

    @Test
    void shouldThrowWhenTicketIsNotFound() {
        TicketOutputDto dto = TicketMapper.ToDto(ticket);
        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.empty());
        Assertions.assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.getById(ticket.getId()),
                "Ticket with ID: " + ticket.getId() + " not found");
    }


    @Test
    void shouldThrowIncorrectDateException() {
        TicketCreateDto createDto = TicketCreateDto.builder()
                .categoryId(ticket.getCategory().getId())
                .description(ticket.getDescription())
                .Urgency(ticket.getUrgency().name())
                .desiredDate(LocalDate.now().minusDays(1))
                .build();
        when(categoryService.getById(anyLong())).thenReturn(category);
        when(userService.getById(anyLong())).thenReturn(owner);
        Assertions.assertThrows(
                IncorrectDateException.class,
                () -> ticketService.createTicket(createDto, owner.getId()));
    }

    @Test
    void shouldNotCreateComment() {
        TicketCreateDto createDto = TicketCreateDto.builder()
                .categoryId(ticket.getCategory().getId())
                .description(ticket.getDescription())
                .Urgency(ticket.getUrgency().name())
                .desiredDate(ticket.getDesiredResolutionDate())
                .comment(null)
                .build();
        TicketOutputDto ticketOutputDto = TicketMapper.ToDto(ticket);
        when(categoryService.getById(anyLong())).thenReturn(category);
        when(userService.getById(anyLong())).thenReturn(owner);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        Assertions.assertEquals(ticketOutputDto, ticketService.createTicket(createDto, owner.getId()));
    }

    @Test
    void shouldCreateComment() {
        TicketCreateDto createDto = TicketCreateDto.builder()
                .categoryId(ticket.getCategory().getId())
                .description(ticket.getDescription())
                .Urgency(ticket.getUrgency().name())
                .desiredDate(ticket.getDesiredResolutionDate())
                .comment("comment")
                .build();
        TicketOutputDto ticketOutputDto = TicketMapper.ToDto(ticket);
        when(categoryService.getById(anyLong())).thenReturn(category);
        when(userService.getById(anyLong())).thenReturn(owner);
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        Assertions.assertEquals(ticketOutputDto, ticketService.createTicket(createDto, owner.getId()));
    }

    @Test
    void shouldUpdateTicket() {
        Ticket ticketForUpdate = ticket;
        ticketForUpdate.setState(State.DRAFT);
        TicketEditDto editDto = TicketEditDto.builder()
                .id(ticket.getId())
                .categoryId(ticket.getCategory().getId())
                .name("new name")
                .description("new description")
                .urgency(Urgency.LOW.name())
                .desiredDate(ticket.getDesiredResolutionDate().plusDays(2))
                .build();
        Ticket updatedTicket = TicketMapper.toEntity(editDto);
        updatedTicket.setId(ticket.getId());
        updatedTicket.setOwner(ticket.getOwner());
        updatedTicket.setCreatedOn(ticket.getCreatedOn());
        updatedTicket.setCategory(category);
        updatedTicket.setAttachments(ticket.getAttachments());
        TicketOutputDto updatedDto = TicketMapper.ToDto(updatedTicket);
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);
        Assertions.assertEquals(updatedDto, ticketService.updateTicket(editDto));
    }

    @Test
    void shouldThrowIllegalActionException() {
        TicketEditDto editDto = TicketEditDto.builder()
                .id(ticket.getId())
                .categoryId(ticket.getCategory().getId())
                .name("new name")
                .description("new description")
                .urgency(Urgency.LOW.name())
                .desiredDate(ticket.getDesiredResolutionDate().plusDays(2))
                .build();
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ticket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        Assertions.assertThrows(IllegalActionException.class, () -> ticketService.updateTicket(editDto));
    }

    @Test
    void shouldThrowIllegalActionExceptionWhenChangeState() {
        TicketStateChangeDto changeDto = TicketStateChangeDto.builder()
                .id(ticket.getId())
                .state(State.DONE.toString())
                .build();
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.ofNullable(ticket));
        CustomUserDetails userDetails = new CustomUserDetails(owner);
        Assertions.assertThrows(IllegalActionException.class, () -> ticketService.changeTicketState(changeDto, userDetails));
    }
}
package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.*;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ticket")
@PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
    public PageOutputDto<TicketOutputDto> getAllTickets(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "urgencyNumber") String columnName,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "true") Boolean isAll) {
            SortTicketParametersDto params = SortTicketParametersDto.builder()
                .page(page)
                .size(size)
                .columnName(columnName)
                .direction(direction)
                .keyword(keyword)
                .isAll(isAll).build();
        return ticketService.getAllSortedTickets(params, userDetails);
    }

    @GetMapping("/{id}")
    public TicketOutputDto getTicketById(@PathVariable Long id) {
        return ticketService.getDtoById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER')")
    public TicketOutputDto createTicket(
            @Validated @RequestBody TicketCreateDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ticketService.createTicket(dto, customUserDetails.getId());
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER')")
    public TicketOutputDto updateTicket(@Validated @RequestBody TicketEditDto dto) {
        return ticketService.updateTicket(dto);
    }

    @PutMapping("/state")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
    public TicketOutputDto changeTicketState(@RequestBody TicketStateChangeDto dto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ticketService.changeTicketState(dto, customUserDetails);
    }
}

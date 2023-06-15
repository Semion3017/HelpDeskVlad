package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.*;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ticket")
@PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
public class TicketController {

    private final TicketService ticketService;

//    @GetMapping("/all")
//    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
//    public ResponseEntity<List<TicketOutputDto>> getAllTickets(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        return ResponseEntity.ok(ticketService.getAllTickets(customUserDetails));
//    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
    public ResponseEntity<List<TicketOutputDto>> getAllTickets(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = true, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "id") String columnName,
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
        return ResponseEntity.ok(ticketService.getAllSortedTickets(params, userDetails).getContent());
    }

//    @GetMapping("/my")
//    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
//    public ResponseEntity<List<TicketOutputDto>> getUserTickets(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
//        return ResponseEntity.ok(ticketService.getByUserId(customUserDetails));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketOutputDto> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER')")
    public ResponseEntity<TicketOutputDto> createTicket(
            @Validated @RequestBody TicketCreateDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    ) {
        return ResponseEntity.ok(ticketService.createTicket(dto, customUserDetails.getId()));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER')")
    public ResponseEntity<TicketOutputDto> updateTicket(@Validated @RequestBody TicketEditDto dto) {
        return ResponseEntity.ok(ticketService.updateTicket(dto));
    }

    @PutMapping("/state")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
    public ResponseEntity<TicketOutputDto> changeTicketState(@RequestBody TicketStateChangeDto dto, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(ticketService.changeTicketState(dto, customUserDetails.getId()));
    }
}

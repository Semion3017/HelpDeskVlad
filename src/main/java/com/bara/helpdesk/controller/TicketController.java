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

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
    public ResponseEntity<List<TicketOutputDto>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }
    @GetMapping("/all/s")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
    public ResponseEntity<List<TicketOutputDto>> getAllTickets(
            @RequestParam(required = true) int page,
            @RequestParam int size,
            @RequestParam String columnName,
            @RequestParam String direction) {
        return ResponseEntity.ok(ticketService.getAllSortedTickets(page, size, columnName, direction).getContent());
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
    public ResponseEntity<List<TicketOutputDto>> getUserTickets(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(ticketService.getByUserId(customUserDetails.getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketOutputDto> getTicketById(@PathVariable Long id){
        return ResponseEntity.ok(ticketService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER')")
    public ResponseEntity<TicketOutputDto> createTicket(
            @Validated @RequestBody TicketCreateDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    )
    {
        return ResponseEntity.ok(ticketService.createTicket(dto, customUserDetails.getId()));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER')")
    public ResponseEntity<TicketOutputDto> updateTicket(@Validated @RequestBody TicketEditDto dto){
        return ResponseEntity.ok(ticketService.updateTicket(dto));
    }

    @PutMapping("/state")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
    public ResponseEntity<String> changeTicketState(@RequestBody TicketStateChangeDto dto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(ticketService.changeTicketState(dto, customUserDetails.getId()));
    }
}

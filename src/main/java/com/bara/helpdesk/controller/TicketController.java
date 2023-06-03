package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.TicketCreateDto;
import com.bara.helpdesk.dto.TicketEditDto;
import com.bara.helpdesk.dto.TicketOutputDto;
import com.bara.helpdesk.dto.TicketStateChangeDto;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestBody TicketCreateDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails
    )
    {
        return ResponseEntity.ok(ticketService.createTicket(dto, customUserDetails.getId()));
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER')")
    public ResponseEntity<TicketOutputDto> updateTicket(@RequestBody TicketEditDto dto){
        return ResponseEntity.ok(ticketService.updateTicket(dto));
    }

    @PutMapping("/state")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER', 'ENGINEER')")
    public ResponseEntity<String> changeTicketState(@RequestBody TicketStateChangeDto dto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(ticketService.changeTicketState(dto, customUserDetails.getId()));
    }
}

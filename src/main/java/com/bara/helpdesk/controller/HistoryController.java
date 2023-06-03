package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.HistoryOutputDto;
import com.bara.helpdesk.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<HistoryOutputDto>> getAllByTicketId(@PathVariable Long id){
        return ResponseEntity.ok(historyService.getByTicketId(id));
    }
}

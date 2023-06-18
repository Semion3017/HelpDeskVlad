package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.HistoryOutputDto;
import com.bara.helpdesk.dto.PageOutputDto;
import com.bara.helpdesk.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public PageOutputDto<HistoryOutputDto> getAllByTicketId(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        return historyService.getByTicketId(id, page, size);
    }
}

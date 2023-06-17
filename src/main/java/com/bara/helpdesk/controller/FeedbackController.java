package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.FeedbackInputDto;
import com.bara.helpdesk.dto.FeedbackOutputDto;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER')")
    public FeedbackOutputDto createFeedback(
            @Validated @RequestBody FeedbackInputDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails){
        return feedbackService.create(dto, userDetails.getId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public FeedbackOutputDto getFeedbackByTicketId(@PathVariable Long id){
        return feedbackService.getByTicketId(id);
    }
}

package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.FeedbackInputDto;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER')")
    public ResponseEntity createFeedback(FeedbackInputDto dto, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ResponseEntity.ok(feedbackService.create(dto, customUserDetails.getId()));
    }
}

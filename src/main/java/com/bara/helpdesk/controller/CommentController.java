package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.CommentInputDto;
import com.bara.helpdesk.dto.CommentOutputDto;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public CommentOutputDto createComment(
            @Validated @RequestBody CommentInputDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        return commentService.createComment(dto, customUserDetails.getId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public Page<CommentOutputDto> getAllByTicketId(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        return commentService.getAllByTicketId(id, page, size);
    }
}

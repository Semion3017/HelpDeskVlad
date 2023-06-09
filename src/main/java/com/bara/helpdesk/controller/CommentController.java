package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.CommentInputDto;
import com.bara.helpdesk.dto.CommentOutputDto;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentOutputDto> createComment(
            @Validated @RequestBody CommentInputDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails)
    {
        return ResponseEntity.ok(commentService.createComment(dto, customUserDetails.getId()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CommentOutputDto>> getAllByTicketId(@PathVariable Long id){
        return ResponseEntity.ok(commentService.getAllByTicketId(id));
    }
}

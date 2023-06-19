package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.AttachmentOutputDto;
import com.bara.helpdesk.dto.AttachmentsDeleteDto;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/attachment")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping("/{ticketId}")
    @PreAuthorize("isAuthenticated()")
    ResponseEntity<List<AttachmentOutputDto>> getAllAttachments(@PathVariable Long ticketId) {
        return ResponseEntity.ok(attachmentService.getAllByTicketId(ticketId));
    }

    @PostMapping("/{ticketId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER')")
    HttpStatus addAttachments(
            @PathVariable Long ticketId,
            @RequestBody List<MultipartFile> files,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        attachmentService.validatedSaveAllAttachments(userDetails, ticketId, files);
        return HttpStatus.CREATED;
    }

    @GetMapping("/download/{id}")
    ResponseEntity<Resource> download(@PathVariable Long id) {
        return attachmentService.getById(id);
    }

    @DeleteMapping("/{ticketId}")
    @PreAuthorize("hasAnyAuthority('MANAGER', 'OWNER')")
    String deleteAllAttachmentsById(
            @PathVariable Long ticketId,
            @RequestBody AttachmentsDeleteDto dto,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return attachmentService.deleteAllById(dto.getIdList(), ticketId, customUserDetails);
    }
}

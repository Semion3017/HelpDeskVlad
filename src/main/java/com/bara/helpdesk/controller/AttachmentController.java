package com.bara.helpdesk.controller;

import com.bara.helpdesk.dto.AttachmentOutputDto;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/attachment")
public class AttachmentController {
    private final Logger logger = LoggerFactory.getLogger(AttachmentController.class);

    private final AttachmentService attachmentService;

    @GetMapping("/{ticketId}")
    ResponseEntity<List<AttachmentOutputDto>> getAllAttachments(@PathVariable Long ticketId) {
        return ResponseEntity.ok(attachmentService.getAllByTicketId(ticketId));
    }

    @PostMapping("/{ticketId}")
    ResponseEntity<AttachmentOutputDto> addAttachment(@PathVariable Long ticketId, @RequestBody MultipartFile file, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(attachmentService.save(ticketId, file, customUserDetails));
    }

    @GetMapping("/download/{id}")
    ResponseEntity<Resource> download(@PathVariable Long id) {
        return attachmentService.getById(id);
    }
}

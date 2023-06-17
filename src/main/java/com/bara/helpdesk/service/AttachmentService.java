package com.bara.helpdesk.service;

import com.bara.helpdesk.dto.AttachmentOutputDto;
import com.bara.helpdesk.security.CustomUserDetails;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
    List<AttachmentOutputDto> getAllByTicketId(Long id);

    void saveAllAttachments(Long ticketId, List<MultipartFile> files);

    void validatedSaveAllAttachments(CustomUserDetails userDetails, Long ticketId, List<MultipartFile> files);

    ResponseEntity<Resource> getById(Long id);

    String deleteAllById(List<Long> idList, Long ticketId, CustomUserDetails userDetails);
}

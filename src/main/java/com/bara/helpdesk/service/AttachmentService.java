package com.bara.helpdesk.service;

import com.bara.helpdesk.dto.AttachmentOutputDto;
import com.bara.helpdesk.security.CustomUserDetails;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
    List<AttachmentOutputDto> getAllByTicketId(Long id);

    AttachmentOutputDto save(Long ticketId, MultipartFile file, CustomUserDetails customUserDetails);

    ResponseEntity<Resource> getById(Long id);
}

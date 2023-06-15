package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.AttachmentOutputDto;
import com.bara.helpdesk.dto.exception.*;
import com.bara.helpdesk.entity.Attachment;
import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.User;
import com.bara.helpdesk.mapper.AttachmentMapper;
import com.bara.helpdesk.repository.AttachmentRepository;
import com.bara.helpdesk.repository.TicketRepository;
import com.bara.helpdesk.repository.UserRepository;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("pdf", "doc", "docx", "png", "jpeg", "jpg");
    private static final Long FILE_MAX_SIZE = 50000000L;

    private final AttachmentRepository attachmentRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Override
    public List<AttachmentOutputDto> getAllByTicketId(Long ticketId) {
        return attachmentRepository.findAllByTicketId(ticketId).stream().map(AttachmentMapper::toDto).collect(Collectors.toList());
    }

    @SneakyThrows(IOException.class)
    @Override
    public AttachmentOutputDto save(Long ticketId, MultipartFile file, CustomUserDetails customUserDetails) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new TicketNotFoundException("Ticket with id: " + ticketId + " notFound"));
        User actor = userRepository.findById(customUserDetails.getId()).orElseThrow(() -> new UserNotFoundException("User with id: " + customUserDetails.getId() + " notFound"));
        String extension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        if (!Objects.equals(ticket.getOwner(), actor)) {
            throw new IllegalActionException();
        }
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalFileExtensionException();
        }
        if (file.getSize() > FILE_MAX_SIZE) {
            throw new FileSizeException();
        }
        Attachment newAttachment = Attachment.builder()
                .blob(file.getBytes())
                .name(file.getOriginalFilename())
                .ticketId(ticketId)
                .build();
        return AttachmentMapper.toDto(attachmentRepository.save(newAttachment));
    }

    @Override
    public ResponseEntity<Resource> getById(Long id) {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(); //TODO exception
        byte[] array = attachment.getBlob();

        ByteArrayResource resource = new ByteArrayResource(array);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment()
                                .filename(attachment.getName())
                                .build().toString())
                .body(resource);
    }
}

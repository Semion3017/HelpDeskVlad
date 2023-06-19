package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.AttachmentOutputDto;
import com.bara.helpdesk.dto.exception.*;
import com.bara.helpdesk.entity.Attachment;
import com.bara.helpdesk.entity.Ticket;
import com.bara.helpdesk.entity.User;
import com.bara.helpdesk.entity.enums.State;
import com.bara.helpdesk.mapper.AttachmentMapper;
import com.bara.helpdesk.repository.AttachmentRepository;
import com.bara.helpdesk.security.CustomUserDetails;
import com.bara.helpdesk.service.AttachmentService;
import com.bara.helpdesk.service.TicketService;
import com.bara.helpdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
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
    private final TicketService ticketService;
    private final UserService userService;

    @Override
    public List<AttachmentOutputDto> getAllByTicketId(Long ticketId) {
        return attachmentRepository.findAllByTicketId(ticketId).stream().map(AttachmentMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void saveAllAttachments(Long ticketId, List<MultipartFile> files) {
        files.forEach((file) -> save(ticketId, file));
    }

    @Override
    public void validatedSaveAllAttachments(CustomUserDetails userDetails, Long ticketId, List<MultipartFile> files) {
        Ticket ticket = ticketService.getById(ticketId);
        User actor = userService.getById(userDetails.getId());
        if ((!Objects.equals(ticket.getOwner(), actor)) || !State.DRAFT.equals(ticket.getState())) {
            throw new IllegalActionException();
        }
        if (!Objects.equals(null, files)) {
            saveAllAttachments(ticketId, files);
        }
    }

    @SneakyThrows(IOException.class)
    private void save(Long ticketId, MultipartFile file) {
        String extension = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf('.') + 1);
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
        attachmentRepository.save(newAttachment);
    }

    @Override
    public ResponseEntity<Resource> getById(Long id) {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new AttachmentNotFoundException("Attachment with id: " + id + " not found"));
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

    @Override
    public String deleteAllById(List<Long> idList, Long ticketId, CustomUserDetails userDetails) {
        Ticket ticket = ticketService.getById(ticketId);
        if ((!Objects.equals(ticket.getOwner().getId(), userDetails.getId())) || !State.DRAFT.equals(ticket.getState())) {
            throw new IllegalActionException();
        }
        List<Long> validatedIdListToDelete = ticket.getAttachments().stream()
                .map(Attachment::getId)
                .filter(idList::contains)
                .toList();
        attachmentRepository.deleteAllByIdInBatch(validatedIdListToDelete);
        return "Attachment was removed";
    }
}

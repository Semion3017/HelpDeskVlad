package com.bara.helpdesk.mapper;

import com.bara.helpdesk.dto.AttachmentOutputDto;
import com.bara.helpdesk.entity.Attachment;

public class AttachmentMapper {
    public static AttachmentOutputDto toDto(Attachment attachment){
        return AttachmentOutputDto.builder()
                .id(attachment.getId())
                .name(attachment.getName())
                .ticket_id(attachment.getTicketId())
                .build();
    }
}

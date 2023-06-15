package com.bara.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AttachmentOutputDto {
    private Long id;
    private Long ticket_id;
    private String name;
}

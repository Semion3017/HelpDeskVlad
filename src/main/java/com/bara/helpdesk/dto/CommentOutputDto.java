package com.bara.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentOutputDto {
    private Long id;

    private LocalDateTime date;

    private String email;

    private String text;
}

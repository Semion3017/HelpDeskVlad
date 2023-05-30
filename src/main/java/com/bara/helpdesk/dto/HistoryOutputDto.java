package com.bara.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class HistoryOutputDto {
    private LocalDateTime date;

    private String action;

    private String email;

    private String description;
}

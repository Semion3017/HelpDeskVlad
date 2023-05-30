package com.bara.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class TicketCreateDto {

    private Long id;

    private Long categoryId;

    private String name;

    private String description;

    private String Urgency;

    private LocalDate desiredDate;

    private String state;

    // todo add attachment

    private String comment;
}

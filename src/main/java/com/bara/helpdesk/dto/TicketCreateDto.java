package com.bara.helpdesk.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class TicketCreateDto {

    @NotNull
    private Long categoryId;
    @NotNull
    private String name;

    private String description;

    @NotNull
    private String Urgency;

    @NotNull
    private LocalDate desiredDate;

    @NotNull
    private String state;

    // todo add attachment

    private String comment;
}

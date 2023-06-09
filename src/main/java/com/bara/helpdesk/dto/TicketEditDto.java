package com.bara.helpdesk.dto;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class TicketEditDto {
    @NotNull
    private Long id;

    @NotNull
    private Long categoryId;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private String urgency;

    @NotNull
    private LocalDate desiredDate;

    @NotNull
    private String state;

    //TODO add attachment
}

package com.bara.helpdesk.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TicketStateChangeDto {

    @NotNull
    private Long id;

    @NotNull
    private String state;
}

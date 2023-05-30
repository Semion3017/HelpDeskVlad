package com.bara.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class TicketOutputDto {
    private Long id;

    private String name;

    private String description;

    private LocalDate createdOn;

    private LocalDate desiredDate;

    private UserDto assignee;

    private UserDto owner;

    private UserDto approver;

    private CategoryDto category;

    private String urgency;

    private String state;


}

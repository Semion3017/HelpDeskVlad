package com.bara.helpdesk.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

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

    private String comment;
}

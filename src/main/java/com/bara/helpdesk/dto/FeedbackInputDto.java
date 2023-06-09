package com.bara.helpdesk.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FeedbackInputDto {

    @NotNull
    private Integer rate;

    @NotNull
    private String text;

    @NotNull
    private Long ticketId;

}

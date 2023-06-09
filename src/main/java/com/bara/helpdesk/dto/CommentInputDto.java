package com.bara.helpdesk.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
public class CommentInputDto {
    @Length(min = 1, max = 500)
    @NotNull
    private String text;

    @NotNull
    private Long ticketId;
}

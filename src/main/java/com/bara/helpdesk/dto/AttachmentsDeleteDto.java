package com.bara.helpdesk.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentsDeleteDto {
    List<Long> idList;
}

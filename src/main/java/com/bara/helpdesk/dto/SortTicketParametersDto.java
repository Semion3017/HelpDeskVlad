package com.bara.helpdesk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SortTicketParametersDto {
    Integer page;
    Integer size;
    String columnName;
    String direction;
    String keyword;
    Boolean isAll;
}

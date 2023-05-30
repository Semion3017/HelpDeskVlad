package com.bara.helpdesk.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

}

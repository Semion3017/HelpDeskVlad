package com.bara.helpdesk.mapper;

import com.bara.helpdesk.dto.UserDto;
import com.bara.helpdesk.entity.User;


public class UserMapper {

    public static UserDto toDto(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}

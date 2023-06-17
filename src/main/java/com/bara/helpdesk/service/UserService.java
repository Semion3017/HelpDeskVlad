package com.bara.helpdesk.service;

import com.bara.helpdesk.entity.User;
import com.bara.helpdesk.entity.enums.Role;

import java.util.List;

public interface UserService {
    User getById(Long id);

    List<User> getByRole(Role role);
}

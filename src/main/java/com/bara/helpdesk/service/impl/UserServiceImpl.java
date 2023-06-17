package com.bara.helpdesk.service.impl;

import com.bara.helpdesk.dto.exception.UserNotFoundException;
import com.bara.helpdesk.entity.User;
import com.bara.helpdesk.entity.enums.Role;
import com.bara.helpdesk.repository.UserRepository;
import com.bara.helpdesk.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with " + id + " not found"));
    }

    @Override
    public List<User> getByRole(Role role) {
        return userRepository.findByRole(role);
    }
}

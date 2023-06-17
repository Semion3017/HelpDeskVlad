package com.bara.helpdesk.security.auth;


import com.bara.helpdesk.dto.exception.AlreadyExistsException;
import com.bara.helpdesk.dto.exception.PasswordRequirementsException;
import com.bara.helpdesk.dto.exception.UserNotFoundException;
import com.bara.helpdesk.entity.User;
import com.bara.helpdesk.entity.enums.Role;
import com.bara.helpdesk.repository.UserRepository;
import com.bara.helpdesk.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final Pattern pattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~.\"(),:;<>@!#$%&'*+-/=?^_`{|}]).{6,20})");

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(validate(request.getPassword())))
                .role(Role.OWNER)
                .build();
        userRepository.save(user);
        String jwtToken = generateToken(request.getEmail());
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String jwtToken = generateToken(request.getEmail());
        return AuthenticationResponse.builder().token(jwtToken).build();
    }


    private String generateToken(String email) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found"))
                .getRole());
        return jwtService.generateToken(
                extraClaims,
                userDetailsService.loadUserByUsername(email)
        );
    }

    private String validate(String password) {
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new PasswordRequirementsException();
        }
        return password;
    }
}

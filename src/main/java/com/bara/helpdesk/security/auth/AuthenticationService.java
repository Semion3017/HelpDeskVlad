package com.bara.helpdesk.security.auth;


import com.bara.helpdesk.dto.exception.AlreadyExistsException;
import com.bara.helpdesk.dto.exception.EmailRequirementsException;
import com.bara.helpdesk.dto.exception.PasswordRequirementsException;
import com.bara.helpdesk.dto.exception.UserNotFoundException;
import com.bara.helpdesk.entity.User;
import com.bara.helpdesk.entity.enums.Role;
import com.bara.helpdesk.repository.UserRepository;
import com.bara.helpdesk.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
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

    private final Pattern passwordPattern = Pattern.compile("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~.\"(),:;<>@!#$%&'*+-/=?^_`{|}]).{6,20})");
    private final Pattern emailPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");

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
                .email(validateEmail(request.getEmail()))
                .password(passwordEncoder.encode(validatePassword(request.getPassword())))
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

    private String validatePassword(String password) {
        Matcher matcher = passwordPattern.matcher(password);
        if (!matcher.matches()) {
            throw new PasswordRequirementsException();
        }
        return password;
    }
    private String validateEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        if (!matcher.matches()) {
            throw new EmailRequirementsException();
        }
        return email;
    }
}

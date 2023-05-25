package com.bara.helpdesk.security.auth;


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

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthenticationResponse register(RegisterRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_OWNER)
                .build();
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(
                getRole(request.getEmail()),
                userDetailsService.loadUserByUsername(user.getEmail())
        );
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()) // todo controller advice
        );

        String jwtToken = jwtService.generateToken(
                getRole(request.getEmail()),
                userDetailsService.loadUserByUsername(request.getEmail())
        );
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private Map<String, Object> getRole(String email){
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", userRepository.findByEmail(email).orElseThrow().getRole());
        return extraClaims;
    }
}

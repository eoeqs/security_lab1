package org.eoeqs.service;

import org.eoeqs.dtos.AuthResponse;
import org.eoeqs.dtos.LoginRequest;
import org.eoeqs.dtos.RegisterRequest;
import org.eoeqs.models.User;
import org.eoeqs.repository.UserRepository;
import org.eoeqs.security.jwt.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("User with email " + request.email() + " already exists");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setName(request.name());

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );
            User user = userRepository.findByEmail(request.email()).orElseThrow();
            String jwtToken = jwtService.generateToken(user);
            return new AuthResponse(jwtToken);
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}
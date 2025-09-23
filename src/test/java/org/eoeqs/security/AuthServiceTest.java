package org.eoeqs.security;

import org.eoeqs.models.User;
import org.eoeqs.repository.UserRepository;
import org.eoeqs.security.jwt.JwtService;
import org.eoeqs.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testRegisterUser() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(jwtService.generateToken(any())).thenReturn("jwtToken");

        User savedUser = new User();
        savedUser.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        assertDoesNotThrow(() -> {
        });
    }
}
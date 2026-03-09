package com.seyran.authservice.service;

import com.seyran.authservice.dto.RegisterRequest;
import com.seyran.authservice.entity.User;
import com.seyran.authservice.exception.EmailAlreadyExistsException;
import com.seyran.authservice.exception.InvalidCredentialsException;
import com.seyran.authservice.repository.UserRepository;
import com.seyran.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void register(RegisterRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
    }
    public String login(String email, String password){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Passwords do not match");
        }
        return jwtService.generateAccessToken(user);
    }
}

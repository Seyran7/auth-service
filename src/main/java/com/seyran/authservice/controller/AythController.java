package com.seyran.authservice.controller;

import com.seyran.authservice.dto.RefreshTokenRequest;
import com.seyran.authservice.entity.RefreshToken;
import com.seyran.authservice.service.JwtService;
import com.seyran.authservice.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenService.verifyToken(request.getRefreshToken());

        String accessToken = jwtService.generateToken(refreshToken.getUser().getEmail());

        return ResponseEntity.ok(accessToken);
    }
}
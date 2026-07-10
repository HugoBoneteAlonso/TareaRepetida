package org.example.empresa.controller;

import lombok.RequiredArgsConstructor;
import org.example.empresa.dto.security.AuthResponseDto;
import org.example.empresa.dto.security.LoginRequestDto;
import org.example.empresa.dto.security.RegisterRequestDto;
import org.example.empresa.dto.security.UserResponseDto;
import org.example.empresa.service.impl.CustomUserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CustomUserDetailsService service;

    @PostMapping("/register")
    public UserResponseDto registerUser(@RequestBody RegisterRequestDto request) {
        return service.createUser(request);
    }

    @PostMapping("/login")
    public AuthResponseDto loginUser(@RequestBody LoginRequestDto request) {
        return service.loginUser(request);
    }
}

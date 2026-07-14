package org.example.empresa.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.empresa.dto.security.*;
import org.example.empresa.service.CustomUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final CustomUserDetailsService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto registerUser(@RequestBody RegisterRequestDto request) {
        return service.createUser(request);
    }

    @PostMapping("/login")
    public AuthResponseDto loginUser(@RequestBody LoginRequestDto dto, HttpServletRequest request) {
        return service.loginUser(dto, request.getRemoteAddr());
    }

    @GetMapping("/me")
    public UserResponseDto me(@AuthenticationPrincipal UserDetails details) {
        return service.getMyUser(details.getUsername());
    }

    @PostMapping("/refresh")
    public AuthResponseDto refreshLogin(@RequestBody RefreshTokenRequestDto dto) {
        return service.refreshLogin(dto);
    }
}

package org.example.empresa.service;

import lombok.AllArgsConstructor;
import org.example.empresa.dto.security.*;
import org.example.empresa.entity.Role;
import org.example.empresa.entity.User;
import org.example.empresa.mapper.UserMapper;
import org.example.empresa.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;
    private final LoginRateLimiter rateLimiter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return user;
    }

    public UserResponseDto createUser(RegisterRequestDto dto) {
        User toCreate = mapper.registerToEntity(dto);
        toCreate.setPassword(encoder.encode(dto.getPassword()));
        toCreate.setRole(Role.USER);
        User saved = repository.save(toCreate);

        return mapper.toDto(saved);
    }

    public AuthResponseDto loginUser(LoginRequestDto dto, String ip) {
        rateLimiter.checkLimit(ip);

        User user = repository.findByEmail(dto.getEmail());

        if(user != null && encoder.matches(dto.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            Long expiresIn = jwtService.extractExpiration(token).getTime() - System.currentTimeMillis();
            return new AuthResponseDto(token, expiresIn, refreshToken);
        }
        rateLimiter.failedAttempt(ip);
        throw new UsernameNotFoundException("Email o contraseña incorrectos");
    }

    public UserResponseDto getMyUser(String email) {
        return mapper.toDto(repository.findByEmail(email));
    }

    public AuthResponseDto refreshLogin(RefreshTokenRequestDto dto) {
        String refreshToken = dto.getRefreshToken();
        User user = repository.findByEmail(jwtService.extractUsername(refreshToken));

        if(user == null) {
            throw new UsernameNotFoundException("Email o contraseña incorrectos");
        }

        String token = jwtService.generateToken(user);
        Long expiresIn = jwtService.extractExpiration(token).getTime() - System.currentTimeMillis();

        return new AuthResponseDto(token, expiresIn, refreshToken);
    }
}

package org.example.empresa.service.impl;

import lombok.AllArgsConstructor;
import org.example.empresa.dto.security.AuthResponseDto;
import org.example.empresa.dto.security.LoginRequestDto;
import org.example.empresa.dto.security.RegisterRequestDto;
import org.example.empresa.dto.security.UserResponseDto;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByEmail(username);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return user;
    }

    public UserResponseDto createUser(RegisterRequestDto dto) {
        dto.setPassword(encoder.encode(dto.getPassword()));
        User toCreate = mapper.registerToEntity(dto);
        toCreate.setRole(Role.USER);
        User saved = repository.save(toCreate);

        return mapper.toDto(saved);
    }

    public AuthResponseDto loginUser(LoginRequestDto request) {
        User user = repository.findByEmail(request.getEmail());

        if(user != null && encoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user);
            Long expiresIn = jwtService.extractExpiration(token).getTime() - System.currentTimeMillis();
            return new AuthResponseDto(token, expiresIn);
        }
        throw new UsernameNotFoundException("Email o contraseña incorrectos");
    }
}

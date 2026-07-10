package org.example.empresa.dto.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.empresa.entity.Role;

@Getter
@Setter
@AllArgsConstructor
public class RegisterRequestDto {
    private String email;
    private Role role;
    private String password;
}

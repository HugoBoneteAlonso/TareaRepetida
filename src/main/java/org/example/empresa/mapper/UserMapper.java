package org.example.empresa.mapper;

import org.example.empresa.dto.security.LoginRequestDto;
import org.example.empresa.dto.security.RegisterRequestDto;
import org.example.empresa.dto.security.UserResponseDto;
import org.example.empresa.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserResponseDto toDto(User user);

    @Mapping(target = "enabled", ignore = true)
    User registerToEntity(RegisterRequestDto dto);

    @Mapping(target = "enabled", ignore = true)
    User loginToEntity(LoginRequestDto dto);
}

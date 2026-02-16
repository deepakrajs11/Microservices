package com.deepakraj.user.mapper;

import com.deepakraj.user.dto.*;
import com.deepakraj.user.model.User;

public class UserMapper {

    public static User toEntity(CreateUserRequestDTO dto) {

        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .contact(dto.getContact())
                .address(dto.getAddress())
                .type(dto.getType())
                .build();
    }

    public static UserResponseDTO toDTO(User user) {

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .contact(user.getContact())
                .address(user.getAddress())
                .type(user.getType())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}

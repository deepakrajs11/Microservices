package com.deepakraj.user.dto;

import com.deepakraj.user.model.UserType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String contact;
    private String address;
    private UserType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.deepakraj.order.dto;

import com.deepakraj.order.model.UserType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

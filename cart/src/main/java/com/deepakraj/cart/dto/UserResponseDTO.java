package com.deepakraj.cart.dto;


import com.deepakraj.cart.model.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

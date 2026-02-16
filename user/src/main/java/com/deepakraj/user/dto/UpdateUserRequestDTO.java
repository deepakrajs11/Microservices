package com.deepakraj.user.dto;

import com.deepakraj.user.model.UserType;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateUserRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50)
    private String name;

    @Pattern(regexp = "^[0-9]{10}$", message = "Contact must be 10 digits")
    private String contact;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "User type is required")
    private UserType type;
}

package com.deepakraj.order.client;


import com.deepakraj.order.dto.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserClient {

    @GetMapping("/api/v1/users/{id}")
    UserResponseDTO getUserById(@PathVariable Long id);
}


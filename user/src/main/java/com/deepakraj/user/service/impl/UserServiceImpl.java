package com.deepakraj.user.service.impl;

import com.deepakraj.user.dto.*;
import com.deepakraj.user.mapper.UserMapper;
import com.deepakraj.user.model.User;
import com.deepakraj.user.repository.UserRepository;
import com.deepakraj.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDTO createUser(CreateUserRequestDTO request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = UserMapper.toEntity(request);

        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return UserMapper.toDTO(user);
    }

    @Override
    public Page<UserResponseDTO> getAllUsers(PageRequest pageRequest) {

        return userRepository.findAll(pageRequest)
                .map(UserMapper::toDTO);
    }

    @Override
    public UserResponseDTO updateUser(Long id, UpdateUserRequestDTO request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setName(request.getName());
        user.setContact(request.getContact());
        user.setAddress(request.getAddress());
        user.setType(request.getType());

        return UserMapper.toDTO(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found");
        }

        userRepository.deleteById(id);
    }
}

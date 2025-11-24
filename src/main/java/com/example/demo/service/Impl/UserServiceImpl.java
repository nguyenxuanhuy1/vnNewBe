package com.example.demo.service.Impl;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity getUserInfo(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Override
    public PageResponse<UserResponseDto> searchUsers(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<UserEntity> userPage;
        if (keyword == null || keyword.trim().isEmpty()) {
            userPage = userRepository.findAll(pageable);
        } else {
            userPage = userRepository.findByEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(
                    keyword.trim(), keyword.trim(), pageable
            );
        }

        List<UserResponseDto> dtoList = userPage.getContent().stream()
                .map(u -> {
                    UserResponseDto dto = new UserResponseDto();
                    dto.setId(u.getId());
                    dto.setFullName(u.getFullName());
                    dto.setEmail(u.getEmail());
                    dto.setRole(u.getRole());
                    dto.setCreatedAt(u.getCreatedAt() != null ? u.getCreatedAt().toString() : null);
                    dto.setUpdatedAt(u.getUpdatedAt() != null ? u.getUpdatedAt().toString() : null);
                    return dto;
                })
                .toList();

        return new PageResponse<>(dtoList, userPage.getTotalElements(), size, page);
    }


    private UserResponseDto mapToDto(UserEntity user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(
                user.getCreatedAt() != null ? user.getCreatedAt().toString() : null
        );
        dto.setUpdatedAt(
                user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null
        );
        return dto;
    }
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

}

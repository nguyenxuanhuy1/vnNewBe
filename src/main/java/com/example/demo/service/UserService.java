package com.example.demo.service;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.entity.UserEntity;
import org.springframework.data.domain.Page;

public interface UserService {
    UserEntity getUserInfo(String email);

    PageResponse<UserResponseDto> searchUsers(String keyword, int page, int size);

    void deleteUser(Long id);
}

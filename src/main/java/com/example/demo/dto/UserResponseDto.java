package com.example.demo.dto;


import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private String role;
    private String createdAt;
    private String updatedAt;
}
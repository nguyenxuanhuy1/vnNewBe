package com.example.demo.service.Impl;

import com.example.demo.dto.AuthResponseDto;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.RegisterDto;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public String register(RegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        UserEntity user = UserEntity.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role("USER")
                .build();

        userRepository.save(user);

        return "Đăng ký tài khoản thành công!";
    }

    public AuthResponseDto login(LoginDto dto) {
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> badCredentials());

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw badCredentials();
        }

        return new AuthResponseDto(
                jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole()),
                jwtUtil.generateRefreshToken(user.getEmail()),
                user.getEmail(),
                user.getRole()
        );
    }
    private ResponseStatusException badCredentials() {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sai tên đăng nhập hoặc mật khẩu");
    }


    public String refreshAccessToken(String refreshToken) {
        // Lấy email từ refresh token
        String email = jwtUtil.extractEmail(refreshToken);

        // Check refresh token hết hạn chưa
        if (jwtUtil.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token has expired, please login again.");
        }

        // Lấy thông tin user từ DB
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Sinh access token mới
        return jwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
    }
}

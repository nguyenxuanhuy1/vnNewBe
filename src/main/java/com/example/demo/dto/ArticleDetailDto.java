package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDetailDto {
    private Long id;
    private String title;
    private String slug;
    private String content;
    private String shortContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long views;
    private List<String> tags;
    private Long categoryId;
    private String image;
}
package com.example.demo.dto;

import com.example.demo.util.ArticleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListDto {
    private Long id;
    private String title;
    private String slug;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long views;
    private Boolean isFeatured;
    private String shortContent;
    private ArticleStatus status;
}
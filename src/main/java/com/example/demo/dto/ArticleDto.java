package com.example.demo.dto;


import lombok.Data;

import java.util.List;

@Data
public class ArticleDto {
    private String title;
    private String slug;
    private String shortContent;
    private String content;
    private String image;
    private Long categoryId;
    private Long views;
    private Boolean isFeatured;
    private List<String> tags;
}
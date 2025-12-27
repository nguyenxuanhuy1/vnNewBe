package com.example.demo.service;
import com.example.demo.dto.*;
import com.example.demo.util.ArticleStatus;

import java.util.List;
public interface ArticleService {
    String createArticle(ArticleDto dto);
    ArticleDetailDto getArticleDetail(String slug);
    String updateArticle(Long id, ArticleDto dto);
    PageResponse<ArticleListDto> getArticlesByCategory(Long categoryId, int page, int size);
    PageResponse<ArticleListDto> searchArticlesByTags(List<String> tags, int page, int size);
    List<CategoryDto> getAllCategories();
    String deleteArticle(Long id);
    CategoryDto createCategory(CategoryDto dto);
    CategoryDto updateCategory(Long id, CategoryDto dto);
    void deleteCategory(Long id);
    PageResponse<ArticleListDto> searchArticlesByTitle(String title, ArticleStatus status, int page, int size);
    void changeStatus(Long articleId, ArticleStatus status);
}
package com.example.demo.service;
import com.example.demo.dto.ArticleDetailDto;
import com.example.demo.dto.ArticleDto;
import com.example.demo.dto.ArticleListDto;
import com.example.demo.dto.PageResponse;
import com.example.demo.entity.Article;

import java.util.List;
import java.util.Map;
public interface ArticleService {
    String createArticle(ArticleDto dto);
    ArticleDetailDto getArticleDetail(String slug);
    String updateArticle(Long id, ArticleDto dto);
    PageResponse<ArticleListDto> getArticlesByCategory(Long categoryId, int page, int size);
    PageResponse<ArticleListDto> searchArticlesByTags(List<String> tags, int page, int size);


}
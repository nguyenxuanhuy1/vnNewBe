package com.example.demo.controller;

import com.example.demo.dto.ArticleDetailDto;
import com.example.demo.dto.ArticleDto;
import com.example.demo.dto.ArticleListDto;
import com.example.demo.dto.PageResponse;
import com.example.demo.entity.Article;
import com.example.demo.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("create")
    public ResponseEntity<String> createArticle(@RequestBody ArticleDto dto) {
        String message = articleService.createArticle(dto);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateArticle(@PathVariable Long id, @RequestBody ArticleDto dto) {
        String message = articleService.updateArticle(id, dto);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ArticleDetailDto> getArticleDetail(@PathVariable String slug) {
        return ResponseEntity.ok(articleService.getArticleDetail(slug));
    }

    @GetMapping("/by-category")
    public PageResponse<ArticleListDto> getArticlesByCategory(
            @RequestParam Long categoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return articleService.getArticlesByCategory(categoryId, page, size);
    }

    @GetMapping("/search-by-tags")
    public PageResponse<ArticleListDto> searchArticlesByTags(
            @RequestParam List<String> tags,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return articleService.searchArticlesByTags(tags, page, size);
    }

}

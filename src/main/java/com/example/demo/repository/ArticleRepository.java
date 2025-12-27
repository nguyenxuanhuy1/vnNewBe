package com.example.demo.repository;

import com.example.demo.entity.Article;
import com.example.demo.entity.Category;
import com.example.demo.util.ArticleStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findBySlug(String slug);
    @Modifying
    @Transactional
    @Query("UPDATE Article a SET a.isFeatured = false WHERE a.isFeatured = true")
    void unsetFeaturedArticles();
    @Query("SELECT DISTINCT a FROM Article a JOIN a.tags t WHERE t.slug IN :slugs")
    Page<Article> findDistinctByTags_SlugIn(@Param("slugs") List<String> slugs, Pageable pageable);
    Page<Article> findByCategoryId(Long categoryId, Pageable pageable);
    boolean existsBySlugAndIdNot(String slug, Long id);
    Page<Article> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Article> findByStatus(ArticleStatus status, Pageable pageable);
    Page<Article> findByCategoryIdAndStatus(
            Long categoryId,
            ArticleStatus status,
            Pageable pageable
    );
    Page<Article> findByTitleContainingIgnoreCaseAndStatus(
            String title,
            ArticleStatus status,
            Pageable pageable
    );

}

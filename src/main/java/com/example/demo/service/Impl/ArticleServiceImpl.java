package com.example.demo.service.Impl;

import com.example.demo.dto.*;
import com.example.demo.entity.Article;
import com.example.demo.entity.Category;
import com.example.demo.entity.Tag;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.service.ArticleService;
import com.example.demo.util.ArticleStatus;
import com.example.demo.util.SlugUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional
    public String createArticle(ArticleDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        String slug = SlugUtil.toSlug(dto.getTitle());

        if (articleRepository.findBySlug(slug).isPresent()) {
            throw new RuntimeException("Tiêu đề đã tồn tại!");
        }
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setSlug(slug);
        article.setShortContent(dto.getShortContent());
        article.setContent(dto.getContent());
        article.setImage(dto.getImage());
        article.setCategory(category);
        article.setViews(dto.getViews());
        article.setIsFeatured(dto.getIsFeatured());
        article.setStatus(ArticleStatus.PENDING);
        if (Boolean.TRUE.equals(dto.getIsFeatured())) {
            articleRepository.unsetFeaturedArticles();
        }
        Set<Tag> savedTags = new HashSet<>();
        for (String tagName : dto.getTags()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        newTag.setSlug(SlugUtil.toSlug(tagName));
                        return tagRepository.save(newTag);
                    });
            savedTags.add(tag);
        }
        article.setTags(savedTags);
        articleRepository.save(article);
        return "Thêm mới bài viết thành công!";
    }


    @Override
    @Transactional
    public String updateArticle(Long id, ArticleDto dto) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));

        String newSlug = SlugUtil.toSlug(dto.getTitle());

        boolean exists = articleRepository.existsBySlugAndIdNot(newSlug, id);
        if (exists) {
            throw new RuntimeException("Tiêu đề đã tồn tại");
        }

        article.setTitle(dto.getTitle());
        article.setSlug(newSlug);
        article.setShortContent(dto.getShortContent());
        article.setContent(dto.getContent());
        article.setImage(dto.getImage());
        article.setViews(dto.getViews());
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
        article.setCategory(category);

        if (Boolean.TRUE.equals(dto.getIsFeatured())) {
            if (!Boolean.TRUE.equals(article.getIsFeatured())) {
                articleRepository.unsetFeaturedArticles();
            }
            article.setIsFeatured(true);
        } else {
            article.setIsFeatured(false);
        }

        Set<Tag> savedTags = new HashSet<>();
        for (String tagName : dto.getTags()) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        newTag.setSlug(SlugUtil.toSlug(tagName));
                        return tagRepository.save(newTag);
                    });
            savedTags.add(tag);
        }
        article.setTags(savedTags);

        articleRepository.save(article);
        return "Cập nhật bài viết thành công!";
    }

    //    @Override
//    public PageResponse<ArticleListDto> getArticlesByCategory(Long categoryId, int page, int size) {
//        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//
//        // Nếu có categoryId → lọc theo category, không có → lấy tất cả
//        Page<Article> articlePage = (categoryId != null)
//                ? articleRepository.findByCategoryId(categoryId, pageable)
//                : articleRepository.findAll(pageable);
//
//        List<ArticleListDto> dtoList = articlePage.getContent().stream()
//                .map(a -> new ArticleListDto(
//                        a.getId(),
//                        a.getTitle(),
//                        a.getSlug(),
//                        a.getImage(),
//                        a.getCreatedAt(),
//                        a.getUpdatedAt(),
//                        a.getViews(),
//                        a.getIsFeatured(),
//                        a.getShortContent(),
//                        a.getStatus()
//                ))
//                .toList();
//
//        return new PageResponse<>(dtoList, articlePage.getTotalElements(), size, page);
//    }
    @Override
    public PageResponse<ArticleListDto> getArticlesByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Article> articlePage = (categoryId != null)
                ? articleRepository.findByCategoryIdAndStatus(
                categoryId,
                ArticleStatus.PUBLISHED, //  mặc định public
                pageable
        )
                : articleRepository.findByStatus(
                ArticleStatus.PUBLISHED, //  mặc định public
                pageable
        );

        List<ArticleListDto> dtoList = articlePage.getContent().stream()
                .map(a -> new ArticleListDto(
                        a.getId(),
                        a.getTitle(),
                        a.getSlug(),
                        a.getImage(),
                        a.getCreatedAt(),
                        a.getUpdatedAt(),
                        a.getViews(),
                        a.getIsFeatured(),
                        a.getShortContent(),
                        a.getStatus()
                ))
                .toList();

        return new PageResponse<>(dtoList, articlePage.getTotalElements(), size, page);
    }

    //    @Override
//    public PageResponse<ArticleListDto> searchArticlesByTitle(String title, int page, int size) {
//        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//
//        Page<Article> articlePage;
//        if (title == null || title.trim().isEmpty()) {
//            articlePage = articleRepository.findAll(pageable);
//        } else {
//            articlePage = articleRepository.findByTitleContainingIgnoreCase(title.trim(), pageable);
//        }
//
//        List<ArticleListDto> dtoList = articlePage.getContent().stream()
//                .map(a -> new ArticleListDto(
//                        a.getId(),
//                        a.getTitle(),
//                        a.getSlug(),
//                        a.getImage(),
//                        a.getCreatedAt(),
//                        a.getUpdatedAt(),
//                        a.getViews(),
//                        a.getIsFeatured(),
//                        a.getShortContent(),
//                        a.getStatus()
//                ))
//                .toList();
//
//        return new PageResponse<>(dtoList, articlePage.getTotalElements(), size, page);
//    }
    @Override
    public PageResponse<ArticleListDto> searchArticlesByTitle(
            String title,
            ArticleStatus status,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        boolean hasTitle = title != null && !title.trim().isEmpty();
        boolean hasStatus = status != null;

        Page<Article> articlePage;

        if (hasTitle && hasStatus) {
            articlePage = articleRepository.findByTitleContainingIgnoreCaseAndStatus(
                    title.trim(),
                    status,
                    pageable
            );
        } else if (hasTitle) {
            articlePage = articleRepository.findByTitleContainingIgnoreCase(
                    title.trim(),
                    pageable
            );
        } else if (hasStatus) {
            articlePage = articleRepository.findByStatus(
                    status,
                    pageable
            );
        } else {
            articlePage = articleRepository.findAll(pageable);
        }

        List<ArticleListDto> dtoList = articlePage.getContent().stream()
                .map(a -> new ArticleListDto(
                        a.getId(),
                        a.getTitle(),
                        a.getSlug(),
                        a.getImage(),
                        a.getCreatedAt(),
                        a.getUpdatedAt(),
                        a.getViews(),
                        a.getIsFeatured(),
                        a.getShortContent(),
                        a.getStatus()
                ))
                .toList();

        return new PageResponse<>(
                dtoList,
                articlePage.getTotalElements(),
                size,
                page
        );
    }

    @Override
    public PageResponse<ArticleListDto> searchArticlesByTags(List<String> tags, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Article> articlePage = articleRepository.findDistinctByTags_SlugIn(tags, pageable);

        List<ArticleListDto> dtoList = articlePage.getContent().stream()
                .map(a -> new ArticleListDto(
                        a.getId(),
                        a.getTitle(),
                        a.getSlug(),
                        a.getImage(),
                        a.getCreatedAt(),
                        a.getUpdatedAt(),
                        a.getViews(),
                        a.getIsFeatured(),
                        a.getShortContent(),
                        a.getStatus()
                ))
                .toList();

        return new PageResponse<>(dtoList, articlePage.getTotalElements(), size, page);
    }

    @Override
    public ArticleDetailDto getArticleDetail(String slug) {
        Article article = articleRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));
        return new ArticleDetailDto(
                article.getId(),
                article.getTitle(),
                article.getSlug(),
                article.getContent(),
                article.getShortContent(),
                article.getCreatedAt(),
                article.getUpdatedAt(),
                article.getViews(),
                article.getTags().stream()
                        .map(Tag::getSlug)
                        .toList(),
                article.getCategory() != null ? article.getCategory().getId() : null,
                article.getImage(),
                article.getIsFeatured()
        );
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(c -> new CategoryDto(c.getId(), c.getName()))
                .toList();
    }

    @Override
    public String deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết"));
        article.getTags().clear();
        articleRepository.save(article);
        articleRepository.delete(article);
        return "Xóa bài viết thành công!";
    }

    @Override
    public CategoryDto createCategory(CategoryDto dto) {
        Category category = new Category();
        category.setName(dto.getName());
        categoryRepository.save(category);

        return new CategoryDto(category.getId(), category.getName());
    }

    @Override
    public CategoryDto updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(dto.getName());
        categoryRepository.save(category);

        return new CategoryDto(category.getId(), category.getName());
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }
    @Override
    @Transactional
    public void changeStatus(Long articleId, ArticleStatus status) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Bài viết không tồn tại"));

        if (status == null) {
            throw new RuntimeException("Trạng thái không hợp lệ");
        }

//        if (status == ArticleStatus.PENDING) {
//            throw new RuntimeException("Không thể chuyển về trạng thái PENDING");
//        }

//        if (article.getStatus() != ArticleStatus.PENDING) {
//            throw new RuntimeException("Chỉ bài đang chờ duyệt mới được đổi trạng thái");
//        }

        article.setStatus(status);
    }

}


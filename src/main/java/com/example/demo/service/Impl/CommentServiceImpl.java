package com.example.demo.service.Impl;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentServiceImpl(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Comment addComment(Long articleId, String author, String content) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new RuntimeException("Article not found"));

        if (commentRepository.existsByArticleIdAndAuthor(articleId, author)) {
            throw new RuntimeException("Bạn đã bình luận bài viết này rồi");
        }

        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setAuthor(author);
        comment.setContent(content);

        return commentRepository.save(comment);
    }
    @Override
    public List<Comment> getCommentsByArticle(Long articleId) {
        return commentRepository.findTop5ByArticleIdOrderByCreatedAtDesc(articleId);
    }
}
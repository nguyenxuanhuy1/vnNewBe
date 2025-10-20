package com.example.demo.service.Impl;

import com.example.demo.entity.Article;
import com.example.demo.entity.Comment;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CommentService;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    public CommentServiceImpl(CommentRepository commentRepository, ArticleRepository articleRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
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
    @Override
    public void deleteComment(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Bình luận không tồn tại"));

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
            throw new RuntimeException("Bạn không có quyền xoá bình luận");
        }

        commentRepository.delete(comment);
    }

}
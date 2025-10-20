package com.example.demo.service;

import com.example.demo.entity.Comment;
import java.util.List;

public interface CommentService {
    Comment addComment(Long articleId, String author, String content);
    List<Comment> getCommentsByArticle(Long articleId);
    void deleteComment(Long commentId, String email);

}
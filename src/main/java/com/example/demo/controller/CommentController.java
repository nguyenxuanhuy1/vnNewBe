package com.example.demo.controller;


import com.example.demo.entity.Comment;
import com.example.demo.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{articleId}")
    public ResponseEntity<?> addComment(
            @PathVariable Long articleId,
            @RequestBody Map<String, String> body,
            Authentication authentication
    ) {
        String content = body.get("content");
        String author = authentication.getName();

        try {
            commentService.addComment(articleId, author, content);
            return ResponseEntity.ok(Map.of("message", "Bình luận thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{articleId}")
    public List<Comment> getComments(@PathVariable Long articleId) {
        return commentService.getCommentsByArticle(articleId);
    }
}

package com.example.demo.dto;

import com.example.demo.util.ArticleStatus;

public class ChangeArticleStatusDto {

    private ArticleStatus status;

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }
}

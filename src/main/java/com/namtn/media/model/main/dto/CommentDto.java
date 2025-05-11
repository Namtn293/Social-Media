package com.namtn.media.model.main.dto;

import lombok.Data;

@Data
public class CommentDto {
    private String content;
    private Long postId;
}

package com.namtn.media.model.main.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailVo {
    private Long id;
    private String createdBy;
    private String firstName;
    private String lastName;
    private String avatar;
    private LocalDateTime createdDate;
    private String content;
    private List<Long> media;
    private Long like;
    private Integer comment;
    private boolean liked;
    private List<CommentVo> commentVos;
}

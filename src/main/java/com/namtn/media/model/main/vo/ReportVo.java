package com.namtn.media.model.main.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ReportVo {
    private Long id;
    private String createdBy;
    private LocalDateTime createdDate;
    private String content;
    private Long postId;
    private Long avatar;
    private Long media;
}

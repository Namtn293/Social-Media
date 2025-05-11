package com.namtn.media.model.main.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportDetailVo {
    private String id;
    private String createdBy;
    private LocalDateTime createdDate;
    private String content;
    private Long totalReport;
    private PostDetailVo post;
}

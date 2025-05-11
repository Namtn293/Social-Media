package com.namtn.media.model.main.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoryVo {
    private LocalDateTime createdDate;
    private String contentId;
    private String action;
    private String navigate;
}

package com.namtn.media.model.main.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatisticalUserVo {
    private String firstName;
    private String lastName;
    private String email;
    private Long avatar;
    private String status;
    private LocalDateTime createdDate;
    private Integer totalPost = 0;
    private Integer postDeleted = 0;

    public void increaseTotalPost() {
        this.totalPost++;
    }

    public void increasePostDeleted() {
        this.postDeleted++;
    }
}

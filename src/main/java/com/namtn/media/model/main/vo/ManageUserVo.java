package com.namtn.media.model.main.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ManageUserVo {
    private String firstName;
    private String lastName;
    private String email;
    private String avatar;
    private String status;
    private LocalDateTime createdDate;
}

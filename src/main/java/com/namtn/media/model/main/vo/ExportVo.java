package com.namtn.media.model.main.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExportVo {
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String status;
}

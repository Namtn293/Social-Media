package com.namtn.media.model.main.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoSearchVo {
    private String firstName;
    private String lastName;
    private String email;
    private Long avatar;
    private String status;
    private String createdDate;
}

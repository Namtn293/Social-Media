package com.namtn.media.model.main.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UserInfoDetailVo {
    private String firstName;
    private String lastName;
    private String email;
    private Long avatar;
    private Long coverImage;
    private Integer age;
    private String address;
    private String phoneNumber;
    private Date birth;
    private String gender;
    private String blog;
    private Long following;
    private Long follower;
    private LocalDateTime createdDate;
    private boolean follow;
}

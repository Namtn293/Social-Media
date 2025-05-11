package com.namtn.media.model.main.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfoDetailDto {
    private String firstName;
    private String lastName;
    private Integer age;
    private String address;
    private String phoneNumber;
    private Date birth;
    private String gender;
    private String blog;
}

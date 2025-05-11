package com.namtn.media.entity;

import com.namtn.media.core.util.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MAIN_USER_INFO")
@EqualsAndHashCode(callSuper = false)
public class UserInfo extends EntityBase {
    @Column(unique = true, name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "BIRTH")
    private Date birth;

    @Column(name = "GENDER")
    private String gender;

    @Column(name = "BLOG")
    private String blog;

    @Column(name = "AVATAR")
    private Long avatar;

    @Column(name = "COVER_IMAGE")
    private Long coverImage;
    public static final UserInfo emptyUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail("");
        userInfo.setAvatar(0L);
        userInfo.setAddress("");
        userInfo.setPhoneNumber("");
        userInfo.setBirth(new Date());
        userInfo.setGender("");
        userInfo.setBlog("");
        userInfo.setCoverImage(0L);
        return userInfo;
    };
}

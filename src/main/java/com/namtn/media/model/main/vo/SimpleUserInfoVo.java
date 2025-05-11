package com.namtn.media.model.main.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleUserInfoVo {
    private String email;
    private Long avatar;
    private String firstName;
    private String lastName;
}

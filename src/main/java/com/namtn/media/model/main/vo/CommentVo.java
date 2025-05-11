package com.namtn.media.model.main.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CommentVo {
    private Long id;
    private String createdBy;
    private String createdDate;
    private Long avatar;
    private String content;
    private String firstname;
    private String lastName;

    public CommentVo() {
    }
}

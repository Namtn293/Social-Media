package com.namtn.media.entity;

import com.namtn.media.core.util.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "MAIN_COMMENT")
@Entity
public class Comment extends EntityBase{
    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "IS_DELETED")
    private Boolean isDeleted=false;
}

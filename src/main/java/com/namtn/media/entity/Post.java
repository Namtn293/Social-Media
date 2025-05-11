package com.namtn.media.entity;

import com.namtn.media.core.util.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "MAIN_POST")
public class Post extends EntityBase {
    @Column(name = "IS_DELETED")
    private boolean isDeleted;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CONTENT")
    private String content;
}

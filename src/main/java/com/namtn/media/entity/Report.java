package com.namtn.media.entity;

import com.namtn.media.core.util.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Table(name ="MAIN_REPORT")
@Entity
@Builder
public class Report extends EntityBase {
    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "CONTENT")
    private String content;
}

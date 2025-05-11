package com.namtn.media.entity;

import com.namtn.media.core.util.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Table(name = "MAIN_HISTORY")
@Entity
@Builder
public class History extends EntityBase {
    @Column(name = "CONTENT_ID")
    private Long contentId;

    @Column(name = "ACTION")
    private String action;

    @Column(name = "NAVIGATE")
    private String navigate;
}

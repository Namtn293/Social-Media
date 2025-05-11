package com.namtn.media.core.util;


import com.namtn.media.core.config.ThreadContext;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public class EntityBase implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int ID_MAX_LENGTH = 36;
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CREATED_DATE", updatable = false, nullable = false, columnDefinition = "TIMESTAMP")
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_DATE", columnDefinition = "TIMESTAMP")
    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @Column(name = "CREATED_BY")
    @CreatedBy
    private String createdBy;

    @Column(name = "MODIFIED_BY")
    @LastModifiedBy
    private String modifiedBy;

    public EntityBase() {}

    @PrePersist
    public void onInsert() {
        LocalDateTime dateNow = LocalDateTime.now();
        if (ThreadContext.getCurrentUser() != null) {
            this.createdBy = ThreadContext.getCurrentUser().getUsername();
            this.modifiedBy = ThreadContext.getCurrentUser().getUsername();
        }
        this.createdDate = dateNow;
        this.modifiedDate = dateNow;
    }

    @PreUpdate
    public void onUpdate() {
        if (ThreadContext.getCurrentUser() != null) {
            this.modifiedBy = ThreadContext.getCurrentUser().getUsername();
        }
        this.modifiedDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}

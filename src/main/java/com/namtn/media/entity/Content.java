package com.namtn.media.entity;

import com.namtn.media.core.util.EntityBase;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "MAIN_CONTENT")
@EqualsAndHashCode(callSuper = false)
@Builder
public class Content extends EntityBase {
    @Column(name = "REF")
    private Long ref;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "ENTITY_TYPE")
    private String entityType;

    @Column(name = "CONTENT",columnDefinition = "BYTEA")
    private byte[] content;

    public byte[] getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public Long getRef() {
        return ref;
    }
}

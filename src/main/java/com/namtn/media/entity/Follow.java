package com.namtn.media.entity;

import com.namtn.media.core.util.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Builder
@Data
@Table(name = "MAIN_FOLLOW")
public class Follow extends EntityBase {
    @Column(name = "IMPACT")
    private String impact;

    @Column(name = "TARGET")
    private String target;
}

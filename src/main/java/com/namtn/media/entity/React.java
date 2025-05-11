package com.namtn.media.entity;

import com.namtn.media.core.util.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "MAIN_REACT")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class React extends EntityBase{
    @Column(name = "ref")
    private Long ref;

    @Column(name = "TYPE")
    private String type;
}

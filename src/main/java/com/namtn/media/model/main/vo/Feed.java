package com.namtn.media.model.main.vo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Feed {
    private Long id;
    private String createdBy;
    private String firstName;
    private String lastName;
    private Long avatar;
    private LocalDateTime createdDate;
    private String content;
    private List<Long> media;
    private Long like;
    private Long comment;
    private boolean liked;
}

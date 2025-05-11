package com.namtn.media.model.main.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserFollowSearch {
    @NotEmpty
    private String type;
    @NotEmpty
    private String key;
}

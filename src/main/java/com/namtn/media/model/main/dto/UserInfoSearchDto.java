package com.namtn.media.model.main.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserInfoSearchDto {
    @NotEmpty
    private String key;
}

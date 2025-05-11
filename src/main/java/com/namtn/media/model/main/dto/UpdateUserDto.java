package com.namtn.media.model.main.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateUserDto {
    @NotEmpty
    private String email;
    @NotEmpty
    private String status;
}

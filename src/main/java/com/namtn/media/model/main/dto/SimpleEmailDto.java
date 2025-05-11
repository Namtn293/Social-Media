package com.namtn.media.model.main.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SimpleEmailDto {
    @NotEmpty(message = "Email must not be empty")
    private String email;
}

package com.namtn.media.model.main.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SimpleIdDto {
    @NotEmpty(message = "Id must not be empty")
    private Long id;
}

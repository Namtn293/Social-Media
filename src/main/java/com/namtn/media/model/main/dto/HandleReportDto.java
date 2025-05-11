package com.namtn.media.model.main.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class HandleReportDto {
    @NotEmpty
    private Long id;
    @NotEmpty
    private String actionCode;
}

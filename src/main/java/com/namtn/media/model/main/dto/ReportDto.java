package com.namtn.media.model.main.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportDto {
    @NotEmpty
    private Long postId;

    @NotEmpty
    private String content;
}

package com.namtn.media.model.main.dto;

import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

@Data
public class DownloadContentDto {
    private MediaType type;
    private String fileName;
    private Resource resource;
}

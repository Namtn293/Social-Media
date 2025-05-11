package com.namtn.media.model.main.dto;

import lombok.Data;
import org.springframework.http.MediaType;

@Data
public class ViewContentDto {
    private MediaType type;
    private String fileName;
    private byte[] content;
}

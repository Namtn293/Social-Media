package com.namtn.media.model.main.vo;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.MediaType;

@Data
@Builder
public class ViewContentVo {
    private MediaType mediaType;
    private String fileName;
    private byte[] content;
}

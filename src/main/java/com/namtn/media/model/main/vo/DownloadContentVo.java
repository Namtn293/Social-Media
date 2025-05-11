package com.namtn.media.model.main.vo;

import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
@Data
public class DownloadContentVo {
    private MediaType mediaType;
    private String fileName;
    private Resource resource;
}

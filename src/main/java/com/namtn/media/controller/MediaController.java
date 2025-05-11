package com.namtn.media.controller;

import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.ResponseUtil;
import com.namtn.media.core.util.SuccessResponse;
import com.namtn.media.enumration.EntityEnum;
import com.namtn.media.model.main.dto.SimpleIdDto;
import com.namtn.media.model.main.vo.DownloadContentVo;
import com.namtn.media.model.main.vo.ViewContentVo;
import com.namtn.media.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/media")
public class MediaController {
    private final ContentService contentService;
    @Autowired
    public MediaController(ContentService contentService) {
        this.contentService = contentService;
    }

    @PostMapping("/get")
    public ResponseEntity<Resource> getFile(@RequestBody SimpleIdDto id) throws BusinessException{
        DownloadContentVo vo=contentService.getFileById(id.getId());
        return ResponseEntity.ok()
                .contentType(vo.getMediaType())
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + vo.getFileName() + "\"")
                .body(vo.getResource());
    }

    @PostMapping("/view")
    public ResponseEntity<byte[]> view(@RequestBody SimpleIdDto id) throws BusinessException{
        ViewContentVo vo = contentService.viewImage(id.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(vo.getMediaType());
        return ResponseEntity.ok()
                .headers(headers)
                .body(vo.getContent());
    }

    @PostMapping("/upload")
    public SuccessResponse<String> upload(@RequestParam("file") MultipartFile file,
                                          @RequestParam("type") String type,
                                          @RequestParam("ref") Long ref) throws IOException, BusinessException {
        contentService.saveFile(file, EntityEnum.getByName(type), ref);
        return ResponseUtil.ok("Upload success");
    }
}

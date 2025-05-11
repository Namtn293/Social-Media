package com.namtn.media.service;

import com.namtn.media.core.model.BusinessException;
import com.namtn.media.enumration.EntityEnum;
import com.namtn.media.model.main.vo.DownloadContentVo;
import com.namtn.media.model.main.vo.ViewContentVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ContentService {
    Long saveFile(MultipartFile file, EntityEnum entityEnum,Long ref) throws IOException, BusinessException;
    List<Long> saveFiles(MultipartFile[] files, EntityEnum entityEnum,Long ref)throws IOException, BusinessException;
    DownloadContentVo getFileById(Long id) throws BusinessException;
    List<Long> getAllPostContent(Long id);
    //map<postId,mediaId>
    Map<Long, Long> getContentOfPost(List<Long> ids);
    ViewContentVo viewImage(Long id) throws BusinessException;

    void updatePostCmt(MultipartFile[] files, EntityEnum entityEnum, Long ref) throws IOException,BusinessException;
}

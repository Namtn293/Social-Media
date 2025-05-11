package com.namtn.media.service.impl;

import com.namtn.media.common.Constants;
import com.namtn.media.core.auth.enumration.ExceptionEnum;
import com.namtn.media.core.config.ThreadContext;
import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.SearchUtil;
import com.namtn.media.entity.Content;
import com.namtn.media.entity.UserInfo;
import com.namtn.media.enumration.EntityEnum;
import com.namtn.media.model.main.vo.DownloadContentVo;
import com.namtn.media.model.main.vo.ViewContentVo;
import com.namtn.media.repository.ContentRepo;
import com.namtn.media.repository.UserInfoRepo;
import com.namtn.media.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContentImpl implements ContentService {
    private final long MAX_FILE_SIZE = 512 * 1024;
    private final ContentRepo contentRepo;
    private final UserInfoRepo userInfoRepo;
    @Autowired
    public ContentImpl(ContentRepo contentRepo, UserInfoRepo userInfoRepo) {
        this.contentRepo = contentRepo;
        this.userInfoRepo = userInfoRepo;
    }

    @Override
    public Long saveFile(MultipartFile file, EntityEnum type, Long ref) throws IOException, BusinessException {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BusinessException(ExceptionEnum.FILE_TOO_LARGE);
        }
        Content content = Content.builder()
                .ref(ref)
                .type(file.getContentType())
                .entityType(type.name())
                .content(file.getBytes())
                .build();
        Long id = contentRepo.save(content).getId();
        Specification<UserInfo> speUserInfo=SearchUtil.eq(Constants.User.EMAIL,ThreadContext.getCurrentUser().getUsername());
        UserInfo userInfo=userInfoRepo.findOne(speUserInfo).orElseThrow(()->new BusinessException(ExceptionEnum.EMAIL_NOT_FOUND));
        switch (type) {
            case AVATAR:
                userInfo.setAvatar(id);
                break;
            case COVER:
                userInfo.setCoverImage(id);
                break;
        }
        userInfoRepo.save(userInfo);
        return id;
    }

    @Override
    public List<Long> saveFiles(MultipartFile[] files, EntityEnum entityEnum, Long ref) throws  IOException,BusinessException {
        if (files==null || files.length<1 || (files.length==1 && files[0].isEmpty())){
            return Collections.emptyList();
        } else if (files.length == 1) {
            return Arrays.asList(saveFile(files[0],entityEnum,ref));
        }

        List<Long> ids=new ArrayList<>();
        for (MultipartFile file:files){
            if (file.getSize()>MAX_FILE_SIZE){
                throw new BusinessException(ExceptionEnum.FILE_TOO_LARGE);
            }
            Content content=Content.builder()
                    .ref(ref)
                    .type(file.getContentType())
                    .content(file.getBytes())
                    .entityType(entityEnum.name())
                    .build();
            contentRepo.save(content);
            ids.add(content.getId());
        }
        return ids;
    }

    @Override
    public DownloadContentVo getFileById(Long id) throws BusinessException {
        Content content=contentRepo.findById(id).orElseThrow(()-> new BusinessException(ExceptionEnum.FILE_NOT_FOUND));
        DownloadContentVo downloadContentVo=new DownloadContentVo();
        byte[] source= content.getContent();
        downloadContentVo.setMediaType(MediaType.parseMediaType(content.getType()));
        downloadContentVo.setResource(new ByteArrayResource(source));
        return downloadContentVo;
    }
    //get add id content of post
    @Override
    public List<Long> getAllPostContent(Long id) {
        Specification<Content> spePost=SearchUtil.eq(Constants.ENTITY_TYPE,EntityEnum.POST.name());
        Specification<Content> speRef=SearchUtil.eq(Constants.REF,id);
        List<Content> contents=contentRepo.findAll(speRef.and(spePost));
        if (contents.isEmpty()){
            Collections.emptyList();
        }
        return contents.stream().map(c->c.getId()).collect(Collectors.toList());
    }
    //get content
    @Override
    public Map<Long, Long> getContentOfPost(List<Long> ids) {
        Specification<Content> speRef=SearchUtil.in(Constants.REF,ids);
        Specification<Content> speEntityType=SearchUtil.eq(Constants.ENTITY_TYPE,EntityEnum.POST.name());
        List<Content> contents=contentRepo.findAll(speEntityType.and(speRef));
        Map<Long,Long> vo=new HashMap<>();
        contents.forEach(c->{
            vo.put(c.getRef(),c.getId());
        });
        return vo;
    }

    @Override
    public ViewContentVo viewImage(Long id) throws BusinessException {
        Content content=contentRepo.findById(id).orElseThrow(()->new BusinessException(ExceptionEnum.CONTENT_NOT_FOUND));
        ViewContentVo vo=ViewContentVo.builder()
                .mediaType(MediaType.parseMediaType(content.getType()))
                .content(content.getContent())
                .build();
        return vo;
    }

    @Override
    public void updatePostCmt(MultipartFile[] files, EntityEnum entityEnum, Long ref) throws IOException,BusinessException {
        Specification<Content> speContent=SearchUtil.eq(Constants.ENTITY_TYPE,entityEnum.name());
        Specification<Content> speRef=SearchUtil.eq(Constants.REF,ref);
        Content content=contentRepo.findOne(speContent.and(speRef)).orElseThrow(()->new BusinessException(ExceptionEnum.CONTENT_NOT_FOUND));
        contentRepo.delete(content);
        saveFiles(files,entityEnum,ref);
    }
}

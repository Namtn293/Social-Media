package com.namtn.media.service;

import com.namtn.media.core.model.BusinessException;
import com.namtn.media.entity.Post;
import com.namtn.media.model.main.dto.ReportDto;
import com.namtn.media.model.main.dto.SimpleIdDto;
import com.namtn.media.model.main.vo.Feed;
import com.namtn.media.model.main.vo.PostDetailVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostDetailVo createPost(MultipartFile[] files,String content) throws IOException, BusinessException;
    PostDetailVo getPostDetail(Long id) throws BusinessException;
    void updatePost(Long id,String content,MultipartFile[] files) throws IOException,BusinessException;
    void deletePost(Long id) throws BusinessException;
    void likePost(Long id);
    Post getPostById(Long id) throws BusinessException;
    List<Feed> getNewsFeed() throws BusinessException;
    List<Feed> getMyPosts(String email) throws BusinessException;
    void reportPost(ReportDto dto) throws BusinessException;
    List<Feed> getTrending();
}

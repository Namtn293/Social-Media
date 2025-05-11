package com.namtn.media.service;

import com.namtn.media.core.model.BusinessException;
import com.namtn.media.model.main.dto.CommentDto;
import com.namtn.media.model.main.dto.SimpleIdDto;
import com.namtn.media.model.main.vo.CommentVo;

import java.util.List;

public interface CommentService {
    CommentVo createComment(CommentDto commentDto) throws BusinessException;
    List<CommentVo> getAllComments(SimpleIdDto postId) throws BusinessException;
    void deleteComment(SimpleIdDto id) throws BusinessException;
}

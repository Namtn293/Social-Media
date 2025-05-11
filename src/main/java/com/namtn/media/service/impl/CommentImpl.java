package com.namtn.media.service.impl;

import com.namtn.media.common.Constants;
import com.namtn.media.core.auth.enumration.ExceptionEnum;
import com.namtn.media.core.config.ThreadContext;
import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.SearchUtil;
import com.namtn.media.entity.Comment;
import com.namtn.media.entity.History;
import com.namtn.media.entity.Post;
import com.namtn.media.entity.UserInfo;
import com.namtn.media.model.main.dto.CommentDto;
import com.namtn.media.model.main.dto.SimpleIdDto;
import com.namtn.media.model.main.vo.CommentVo;
import com.namtn.media.model.main.vo.SimpleUserInfoVo;
import com.namtn.media.repository.CommentRepo;
import com.namtn.media.repository.PostRepo;
import com.namtn.media.service.CommentService;
import com.namtn.media.service.HistoryService;
import com.namtn.media.service.UserInfoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentImpl implements CommentService {
    private final CommentRepo commentRepo;
    private final ModelMapper modelMapper;
    private final HistoryService historyService;
    private final UserInfoService userInfoService;
    private final PostRepo postRepo;
    @Autowired
    public CommentImpl(PostRepo postRepo,UserInfoService userInfoService,HistoryService historyService,CommentRepo commentRepo, ModelMapper modelMapper) {
        this.commentRepo = commentRepo;
        this.modelMapper = modelMapper;
        this.historyService = historyService;
        this.userInfoService = userInfoService;
        this.postRepo = postRepo;
    }

    @Override
    public CommentVo createComment(CommentDto commentDto) throws BusinessException{
        Post post=postRepo.findById(commentDto.getPostId()).orElseThrow(()-> new BusinessException(ExceptionEnum.POST_NOT_FOUND));
        History history = History.builder()
                .action("Comment in post")
                .contentId(commentDto.getPostId())
                .navigate("/post/" + commentDto.getPostId())
                .build();
        historyService.create(history);
        Comment comment = modelMapper.map(commentDto, Comment.class);
        comment.setId(null);
        comment.setIsDeleted(false);
        commentRepo.save(comment);

        SimpleUserInfoVo infoVo=userInfoService.getSimpleInfo(comment.getCreatedBy());

        CommentVo vo = new CommentVo();
        vo.setId(comment.getId());
        vo.setContent(comment.getContent());
        vo.setCreatedBy(comment.getCreatedBy());
        vo.setCreatedDate(comment.getCreatedDate().toString());
        vo.setAvatar(infoVo.getAvatar());
        vo.setFirstname(infoVo.getFirstName());
        vo.setLastName(infoVo.getLastName());
        return vo;
    }


    @Override
    public List<CommentVo> getAllComments(SimpleIdDto postId) throws BusinessException {
        Specification<Comment> spePostId = SearchUtil.eq(Constants.POST_ID,postId.getId());
        Specification<Comment> speIsDeleted = SearchUtil.eq(Constants.IS_DELETED,false);
        List<Comment> comments=commentRepo.findAll(spePostId.and(speIsDeleted));
        List<CommentVo> commentVos=new ArrayList<>();
        for(Comment c:comments) {
            CommentVo vo=modelMapper.map(c, CommentVo.class);
            SimpleUserInfoVo user=userInfoService.getSimpleInfo(c.getCreatedBy());
            modelMapper.map(user,vo);
            commentVos.add(vo);
        }
        return commentVos;
    }

    @Override
    public void deleteComment(SimpleIdDto id) throws BusinessException{
        Comment comment=commentRepo.findById(id.getId()).orElseThrow(()->new BusinessException(ExceptionEnum.COMMENT_NOT_FOUND));
        if (comment.getIsDeleted()){
            throw new BusinessException(ExceptionEnum.COMMENT_IS_DELETED);
        }
        if (comment.getCreatedBy().equals(ThreadContext.getCurrentUser().getUsername())){
            comment.setIsDeleted(true);
        } else {
            throw new BusinessException(ExceptionEnum.NOT_PERMISSION);
        }
        commentRepo.save(comment);
    }
}

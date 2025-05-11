package com.namtn.media.service.impl;

import com.namtn.media.common.Constants;
import com.namtn.media.core.auth.enumration.ExceptionEnum;
import com.namtn.media.core.config.ThreadContext;
import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.SearchUtil;
import com.namtn.media.entity.*;
import com.namtn.media.enumration.EntityEnum;
import com.namtn.media.model.main.dto.ReportDto;
import com.namtn.media.model.main.dto.SimpleIdDto;
import com.namtn.media.model.main.dto.TrendingReactDto;
import com.namtn.media.model.main.vo.CommentVo;
import com.namtn.media.model.main.vo.Feed;
import com.namtn.media.model.main.vo.PostDetailVo;
import com.namtn.media.model.main.vo.SimpleUserInfoVo;
import com.namtn.media.repository.*;
import com.namtn.media.service.ContentService;
import com.namtn.media.service.HistoryService;
import com.namtn.media.service.PostService;
import com.namtn.media.service.UserInfoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostImpl implements PostService {
    private final PostRepo postRepo;
    private final ReactRepo reactRepo;
    private final ContentService contentService;
    private final ModelMapper modelMapper;
    private final CommentRepo commentRepo;
    private final FollowRepo followRepo;
    private final ReportRepo reportRepo;
    private final HistoryService historyService;
    private final UserInfoService userInfoService;
    private final HistoryRepo historyRepo;
    @Autowired
    public PostImpl(HistoryRepo historyRepo,UserInfoService userInfoService,PostRepo postRepo, ReactRepo reactRepo, ContentService contentService, ModelMapper modelMapper, CommentRepo commentRepo, FollowRepo followRepo, ReportRepo reportRepo, HistoryService historyService) {
        this.postRepo = postRepo;
        this.reactRepo = reactRepo;
        this.contentService = contentService;
        this.modelMapper = modelMapper;
        this.commentRepo = commentRepo;
        this.followRepo = followRepo;
        this.reportRepo = reportRepo;
        this.historyService = historyService;
        this.userInfoService=userInfoService;
        this.historyRepo=historyRepo;
    }
    @Override
    public PostDetailVo createPost(MultipartFile[] files, String content) throws IOException, BusinessException {
        if ((files == null )) {
            throw new BusinessException(ExceptionEnum.INVALID_INPUT);
        }
        Post post = new Post();
        post.setContent(content);
        Long postId = postRepo.save(post).getId();
        List<Long> contentIds = contentService.saveFiles(files, EntityEnum.POST, postId);
        SimpleUserInfoVo userInfoVo=userInfoService.getSimpleInfo(ThreadContext.getCurrentUser().getUsername());
        PostDetailVo vo = modelMapper.map(post, PostDetailVo.class);
        modelMapper.map(userInfoVo,vo);
        vo.builder()
                .media(contentIds)
                .liked(false)
                .commentVos(new ArrayList<>())
                .like(0L)
                .comment(0)
                .build();
        return vo;
    }

    @Override
    public PostDetailVo getPostDetail(Long id) throws BusinessException {
        Post post=postRepo.findById(id).orElseThrow(()->new BusinessException(ExceptionEnum.POST_NOT_FOUND));
        PostDetailVo postDetailVo=modelMapper.map(post,PostDetailVo.class);
        postDetailVo.setMedia(contentService.getAllPostContent(id));

        String email=ThreadContext.getCurrentUser().getUsername();
        SimpleUserInfoVo infoVo=userInfoService.getSimpleInfo(email);
        modelMapper.map(infoVo,postDetailVo);

        //set like
        Specification<React> speType= SearchUtil.eq(Constants.TYPE,EntityEnum.POST.name());
        Specification<React> speRef=SearchUtil.eq(Constants.REF,id);
        Specification<React> speCreatedBy=SearchUtil.eq(Constants.CREATED_BY,ThreadContext.getCurrentUser().getUsername());
        postDetailVo.setLike(reactRepo.count(speRef.and(speType)));

        //set comment
        Specification<Comment> speCmtId=SearchUtil.eq(Constants.POST_ID,id);
        Specification<Comment> speCmtIsDeleted=SearchUtil.eq(Constants.IS_DELETED,false);
        List<Comment> comments=commentRepo.findAll(speCmtIsDeleted.and(speCmtId));
        postDetailVo.setComment(comments.size());

        //set comments
        List<CommentVo> vos=comments.stream()
                        .map(c-> modelMapper.map(c, CommentVo.class)).collect(Collectors.toList());

        //set liked
        postDetailVo.setLiked(reactRepo.exists(speRef.and(speType.and(speCreatedBy))));


        //add avatar of each comment
        List<String> emails=new ArrayList<>();
        emails.add(post.getCreatedBy());
        comments.forEach(c->emails.add(c.getCreatedBy()));
        Map<String,SimpleUserInfoVo> avatarMap=userInfoService.getSimpleInfo(emails);
        vos.forEach(c->{
            c.setAvatar(avatarMap.get(c.getCreatedBy()).getAvatar());
        });
        postDetailVo.setCommentVos(vos);
        return postDetailVo;
    }

    @Override
    public void updatePost(Long id, String content, MultipartFile[] files) throws IOException,BusinessException {
        Post post=postRepo.findById(id).orElseThrow(()->new BusinessException(ExceptionEnum.POST_NOT_FOUND));
        checkUserPermission(post);
        contentService.updatePostCmt(files,EntityEnum.POST,id);
        post.setContent(content);
        postRepo.save(post);
    }

    @Override
    public void deletePost(Long id) throws BusinessException {
        Post post=postRepo.findById(id).orElseThrow(()->new BusinessException(ExceptionEnum.POST_NOT_FOUND));
        checkUserPermission(post);
        post.setDeleted(true);
        postRepo.save(post);
    }

    @Override
    public void likePost(Long id) {
        Specification<React> speType=SearchUtil.eq(Constants.TYPE,EntityEnum.POST.name());
        Specification<React> speRef=SearchUtil.eq(Constants.REF,id);
        Optional<React> react=reactRepo.findOne(speType.and(speRef));
        History history=History.builder()
                .contentId(id)
                .navigate("/post/"+id)
                .build();
        //if user don't like this post,set like
        if (!react.isPresent()){
            React newReact=reactRepo.save(new React(id,EntityEnum.POST.name()));
            history.setAction(newReact.getCreatedBy()+" liked a post");
        } else {
            history.setAction(react.get().getCreatedBy()+" unliked a post");
            reactRepo.delete(react.get());
        }
        historyRepo.save(history);
    }

    @Override
    public Post getPostById(Long id) throws BusinessException {
        Post post=postRepo.findById(id).orElseThrow(()-> new BusinessException(ExceptionEnum.POST_NOT_FOUND));
        return post;
    }
    //get my posts and all followed posts
    @Override
    public List<Feed> getNewsFeed() throws BusinessException {
        String email=ThreadContext.getCurrentUser().getUsername();
        List<Feed> feeds=getMyPosts(email);
        Specification<Follow> speFollow=SearchUtil.eq(Constants.IMPACT,email);
        List<Follow> follows=followRepo.findAll(speFollow);
        for (Follow c:follows){
            feeds.addAll(getMyPosts(c.getTarget()));
        }
        return feeds;
    }

    @Override
    public List<Feed> getMyPosts(String email) throws BusinessException {
        SimpleUserInfoVo simpleUserInfoVo=userInfoService.getSimpleInfo(email);
        Specification<Post> speDeleted=SearchUtil.eq(Constants.IS_DELETED,false);
        Specification<Post> speUser=SearchUtil.eq(Constants.CREATED_BY,email);
        List<Post> posts=postRepo.findAll(speUser.and(speDeleted));
        List<Feed> feeds=new ArrayList<>();
        posts.forEach(c->{
            Feed feed=modelMapper.map(simpleUserInfoVo,Feed.class);
            modelMapper.map(c,feed);
            //set media
            feed.setMedia(contentService.getAllPostContent(c.getId()));

            Specification<React> speUserReact=SearchUtil.eq(Constants.CREATED_BY,ThreadContext.getCurrentUser().getUsername());
            Specification<React> speType=SearchUtil.eq(Constants.TYPE,EntityEnum.POST.name());
            Specification<React> speRef=SearchUtil.eq(Constants.REF,c.getId());
            //set like
            feed.setLike(reactRepo.count(speType.and(speRef)));
            //set liked
            feed.setLiked(reactRepo.exists(speRef.and(speType.and(speUserReact))));
            //set comment
            Specification<Comment> speComment=SearchUtil.eq(Constants.POST_ID,c.getId());
            feed.setComment(commentRepo.count(speComment));
            feeds.add(feed);
        });
        return feeds;
    }

    @Override
    public void reportPost(ReportDto dto) throws BusinessException {
        String userReport=ThreadContext.getCurrentUser().getUsername();
        Specification<Report> spePostId=SearchUtil.eq(Constants.POST_ID,dto.getPostId());
        Specification<Report> speUserReport=SearchUtil.eq(Constants.CREATED_BY,userReport);
        if (reportRepo.exists(speUserReport.and(spePostId))){
            throw new BusinessException(ExceptionEnum.REPORT_ALREADY_EXIST);
        }
        Report report=Report.builder()
                .postId(dto.getPostId())
                .content(dto.getContent())
                .build();
        reportRepo.save(report);
    }

    @Override
    public List<Feed> getTrending() {
        List<Object[]> top10Trending=reactRepo.getTop10Trending();
        List<TrendingReactDto> dto=new ArrayList<>();
        top10Trending.forEach(c->{
            dto.add(new TrendingReactDto(((Number)c[0]).longValue(),((Number)c[1]).longValue()));
        });
        List<Post> posts=postRepo.findAllById(dto.stream()
                .map(TrendingReactDto::getRef).collect(Collectors.toList()));

        Map<String,SimpleUserInfoVo> users=userInfoService.getSimpleInfo(posts.stream()
                .map(Post::getCreatedBy).collect(Collectors.toList())
        );
        List<Feed> feeds=new ArrayList<>();
        for (Post post:posts){
            Feed feed=modelMapper.map(post,Feed.class);
            modelMapper.map(users.get(post.getCreatedBy()),feed);

            Specification<React> speRef=SearchUtil.eq(Constants.REF,post.getId());
            Specification<React> speType=SearchUtil.eq(Constants.TYPE,EntityEnum.POST.name());
            Specification<React> speUser=SearchUtil.eq(Constants.CREATED_BY,post.getCreatedBy());
            feed.setLike(reactRepo.count(speRef.and(speType)));

            Specification<Comment> speComment=SearchUtil.eq(Constants.POST_ID,post.getId());
            feed.setComment(commentRepo.count(speComment));

            feed.setLiked(reactRepo.exists(speRef.and(speType.and(speUser))));

            feed.setMedia(contentService.getAllPostContent(post.getId()));
            feeds.add(feed);
        }
        return feeds;
    }

    public void checkUserPermission(Post post)throws BusinessException{
        if (!post.getCreatedBy().equals(ThreadContext.getCurrentUser().getUsername())){
            throw new BusinessException(ExceptionEnum.NOT_PERMISSION);
        }
    }
}

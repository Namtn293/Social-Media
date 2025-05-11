package com.namtn.media.service.impl;

import com.namtn.media.common.Constants;
import com.namtn.media.core.auth.enumration.ExceptionEnum;
import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.SearchUtil;
import com.namtn.media.entity.Post;
import com.namtn.media.entity.Report;
import com.namtn.media.enumration.HandleReportEnum;
import com.namtn.media.model.main.dto.HandleReportDto;
import com.namtn.media.model.main.vo.ReportDetailVo;
import com.namtn.media.model.main.vo.ReportVo;
import com.namtn.media.model.main.vo.SimpleUserInfoVo;
import com.namtn.media.repository.ContentRepo;
import com.namtn.media.repository.PostRepo;
import com.namtn.media.repository.ReportRepo;
import com.namtn.media.service.ContentService;
import com.namtn.media.service.PostService;
import com.namtn.media.service.ReportService;
import com.namtn.media.service.UserInfoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReportImpl implements ReportService {
    private final ReportRepo reportRepo;
    private final ModelMapper modelMapper;
    private final UserInfoService userInfoService;
    private final ContentService contentService;
    private final ContentRepo contentRepo;
    private final PostService postService;
    private final PostRepo postRepo;
    @Autowired
    public ReportImpl(PostRepo postRepo,PostService postService,ContentRepo contentRepo,ContentService contentService,UserInfoService userInfoService,ModelMapper modelMapper,ReportRepo reportRepo) {
        this.reportRepo = reportRepo;
        this.modelMapper = modelMapper;
        this.userInfoService = userInfoService;
        this.contentService = contentService;
        this.contentRepo = contentRepo;
        this.postService = postService;
        this.postRepo = postRepo;
    }

    @Override
    public List<ReportVo> getListReport() {
        List<Report> reports = reportRepo.findAll();
        Set<String> emails = reports.stream().map(Report::getCreatedBy).collect(Collectors.toSet());
        Set<Long> postIds = reports.stream().map(Report::getPostId).collect(Collectors.toSet());

        Map<String, SimpleUserInfoVo> userMap = userInfoService.getSimpleInfo(new ArrayList<>(emails));
        Map<Long, Long> postMap = contentService.getContentOfPost(new ArrayList<>(postIds));

        return reports.stream().map(report -> {
            ReportVo vo = modelMapper.map(report, ReportVo.class);
            SimpleUserInfoVo user = userMap.get(report.getCreatedBy());
            if (user != null) vo.setAvatar(user.getAvatar());
            vo.setMedia(postMap.get(report.getPostId()));
            return vo;
        }).collect(Collectors.toList());
    }


    @Override
    public ReportDetailVo getDetailReport(Long id) throws BusinessException {
        Report report=findById(id);
        Specification<Report> speReport= SearchUtil.eq(Constants.POST_ID,report.getPostId());
        ReportDetailVo vo=modelMapper.map(report,ReportDetailVo.class);
        vo.setTotalReport(reportRepo.count(speReport));
        vo.setPost(postService.getPostDetail(report.getPostId()));
        return vo;
    }

    @Override
    public Report findById(Long id) throws BusinessException {
        Report report=reportRepo.findById(id).orElseThrow(()->new BusinessException(ExceptionEnum.REPORT_NOT_FOUND));
        return report;
    }

    @Override
    public void handleReport(HandleReportDto handleReportDto) throws BusinessException{
        Report report=findById(handleReportDto.getId());
        if (handleReportDto.getActionCode().equals(HandleReportEnum.D)){
            Post post=postService.getPostById(report.getPostId());
            post.setDeleted(true);
            postRepo.save(post);
        }
        Specification<Report> speReport=SearchUtil.eq(Constants.POST_ID,report.getPostId());
        List<Report> reports=reportRepo.findAll(speReport);
        reportRepo.deleteAll(reports);
    }
}

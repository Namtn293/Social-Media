package com.namtn.media.model.main.vo;

import lombok.Data;

import java.util.List;

@Data
public class StatisticalVo {
    private Long totalUser;
    private Long totalPost;
    private List<StatisticalUserVo> newUser;
    private Long newPost;}

package com.namtn.media.service;

import com.namtn.media.core.model.BusinessException;
import com.namtn.media.model.main.dto.UserFollowSearch;
import com.namtn.media.model.main.dto.UserInfoDetailDto;
import com.namtn.media.model.main.dto.UserInfoSearchDto;
import com.namtn.media.model.main.vo.SimpleUserInfoVo;
import com.namtn.media.model.main.vo.UserInfoDetailVo;
import com.namtn.media.model.main.vo.UserInfoSearchVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserInfoService {
    void updateInfo(UserInfoDetailDto userInfoDetailDto)throws BusinessException;
    UserInfoDetailVo getUserDetail(String email) throws BusinessException;
    int getAge(Date date);
    void follow(String email);
    List<UserInfoSearchVo> searchUser(UserInfoSearchDto userInfoSearchDto);
    UserInfoDetailVo getMyProfile();
    SimpleUserInfoVo getSimpleInfo(String email) throws BusinessException;
    Map<String, SimpleUserInfoVo> getSimpleInfo(List<String> emails);
    List<UserInfoSearchVo> searchFollowUser(UserFollowSearch userFollowSearch) throws BusinessException;
}

package com.namtn.media.service;

import com.namtn.media.model.main.dto.ManageUserDto;
import com.namtn.media.model.main.dto.StatisticalDto;
import com.namtn.media.model.main.vo.ManageUserVo;
import com.namtn.media.model.main.vo.StatisticalVo;

import java.util.List;

public interface AdminService {
    StatisticalVo statistical(StatisticalDto statisticalDto);
    List<ManageUserVo> managerUser(ManageUserDto manageUserDto);
}

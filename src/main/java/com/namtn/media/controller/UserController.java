package com.namtn.media.controller;

import com.namtn.media.common.Message;
import com.namtn.media.core.auth.service.LogoutService;
import com.namtn.media.core.auth.service.UserService;
import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.ResponseUtil;
import com.namtn.media.core.util.SuccessResponse;
import com.namtn.media.model.main.dto.SimpleEmailDto;
import com.namtn.media.model.main.dto.UserInfoDetailDto;
import com.namtn.media.model.main.vo.UserInfoDetailVo;
import com.namtn.media.service.HistoryService;
import com.namtn.media.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserInfoService userInfoService;
    private UserService userService;
    private LogoutService logoutService;
    private HistoryService historyService;

    @Autowired
    public UserController(UserInfoService userInfoService, UserService userService, LogoutService logoutService, HistoryService historyService) {
        this.userInfoService = userInfoService;
        this.userService = userService;
        this.logoutService = logoutService;
        this.historyService = historyService;
    }

    @PostMapping("/update-info")
    public SuccessResponse<String> updateInfo(@RequestBody UserInfoDetailDto dto) throws BusinessException{
        userInfoService.updateInfo(dto);
        return ResponseUtil.ok(Message.SUCCESS);
    }

    @PostMapping("/profile")
    public SuccessResponse<UserInfoDetailVo> profile(@RequestBody SimpleEmailDto email) throws BusinessException{
        return ResponseUtil.ok(userInfoService.getUserDetail(email.getEmail()));
    }

    @GetMapping("/my-profile")
    public SuccessResponse<UserInfoDetailVo> getMyProfile() throws BusinessException{
        return ResponseUtil.ok(userInfoService.getMyProfile());
    }

    @PostMapping("/follow")
    public SuccessResponse<String> follow(@RequestBody SimpleEmailDto dto) throws BusinessException{
        userInfoService.follow(dto.getEmail());
        return ResponseUtil.ok(Message.SUCCESS);
    }
}

package com.namtn.media.controller;

import com.namtn.media.common.Message;
import com.namtn.media.core.auth.model.dto.ChangePasswordDTO;
import com.namtn.media.core.auth.service.LogoutService;
import com.namtn.media.core.auth.service.UserService;
import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.ResponseUtil;
import com.namtn.media.core.util.SuccessResponse;
import com.namtn.media.model.main.dto.SimpleEmailDto;
import com.namtn.media.model.main.dto.UserFollowSearch;
import com.namtn.media.model.main.dto.UserInfoDetailDto;
import com.namtn.media.model.main.dto.UserInfoSearchDto;
import com.namtn.media.model.main.vo.HistoryVo;
import com.namtn.media.model.main.vo.UserInfoDetailVo;
import com.namtn.media.model.main.vo.UserInfoSearchVo;
import com.namtn.media.service.HistoryService;
import com.namtn.media.service.UserInfoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @PostMapping("/search")
    public SuccessResponse<List<UserInfoSearchVo>> search(@RequestBody UserInfoSearchDto dto){
        return ResponseUtil.ok(userInfoService.searchUser(dto));
    }

    @PostMapping("/logout")
    public SuccessResponse<String> logout(HttpServletRequest request){
        logoutService.logoutCustom(request);
        return ResponseUtil.ok(Message.SUCCESS);
    }

    @PostMapping("/change-password")
    public SuccessResponse<String> changePassword(@RequestBody ChangePasswordDTO dto, Principal connectedUser){
        userService.changePassword(dto,connectedUser);
        return ResponseUtil.ok(Message.SUCCESS);
    }

    @GetMapping("/history")
    public SuccessResponse<List<HistoryVo>> getStory(){
        return ResponseUtil.ok(historyService.getAllHistory());
    }

    @PostMapping("/follow/search")
    public SuccessResponse<List<UserInfoSearchVo>> getFollower(@RequestBody UserFollowSearch search) throws BusinessException{
        return ResponseUtil.ok(userInfoService.searchFollowUser(search));
    }
}

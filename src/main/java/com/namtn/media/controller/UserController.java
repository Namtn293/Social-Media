package com.namtn.media.controller;

import com.namtn.media.core.auth.service.LogoutService;
import com.namtn.media.core.auth.service.UserService;
import com.namtn.media.service.HistoryService;
import com.namtn.media.service.UserInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserInfoService userInfoService;
    private UserService userService;
    private LogoutService logoutService;
    private HistoryService historyService;

}

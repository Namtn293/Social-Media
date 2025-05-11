package com.namtn.media.core.auth.controller;

import com.namtn.media.core.auth.model.dto.AuthenticationDTO;
import com.namtn.media.core.auth.model.dto.RegisterDTO;
import com.namtn.media.core.auth.model.vo.LoginVo;
import com.namtn.media.core.auth.service.AuthenticationService;
import com.namtn.media.core.auth.service.UserService;
import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.ResponseUtil;
import com.namtn.media.core.util.SuccessResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController {
    private AuthenticationService authenticationService;
    private UserService userService;
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public SuccessResponse<LoginVo> register(@RequestBody RegisterDTO registerDTO) throws IllegalAccessException{
        return ResponseUtil.ok(authenticationService.register(registerDTO));
    }

    @PostMapping("/login")
    public SuccessResponse<LoginVo> login(@RequestBody AuthenticationDTO authenticationDTO) throws BusinessException{
        return ResponseUtil.ok(authenticationService.login(authenticationDTO));
    }

    @PostMapping("/admin/login")
    public SuccessResponse<LoginVo> loginAdmin(@RequestBody AuthenticationDTO authenticationDTO) throws BusinessException{
        return ResponseUtil.ok(authenticationService.loginAdmin(authenticationDTO));
    }

    @PostMapping("refresh-token")
    public SuccessResponse<LoginVo> refreshToken(HttpServletRequest request,
                                                 HttpServletResponse response) throws BusinessException, IOException {
        return ResponseUtil.ok(authenticationService.refreshToken(request,response));
    }
}

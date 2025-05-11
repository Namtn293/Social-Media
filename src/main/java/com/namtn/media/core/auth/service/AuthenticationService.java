package com.namtn.media.core.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.namtn.media.common.Constants;
import com.namtn.media.core.auth.entity.Token;
import com.namtn.media.core.auth.entity.User;
import com.namtn.media.core.auth.enumration.ExceptionEnum;
import com.namtn.media.core.auth.enumration.RoleEnum;
import com.namtn.media.core.auth.enumration.TokenEnum;
import com.namtn.media.core.auth.enumration.UserStatusEnum;
import com.namtn.media.core.auth.model.dto.AuthenticationDTO;
import com.namtn.media.core.auth.model.dto.RegisterDTO;
import com.namtn.media.core.auth.model.vo.LoginVo;
import com.namtn.media.core.auth.repository.TokenRepository;
import com.namtn.media.core.auth.repository.UserRepository;
import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.CommonUtil;
import com.namtn.media.core.util.SearchUtil;
import com.namtn.media.entity.UserInfo;
import com.namtn.media.repository.UserInfoRepo;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserInfoRepo userInfoRepo;
    @Autowired
    public AuthenticationService(UserInfoRepo userInfoRepo,UserService userService, UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.modelMapper = modelMapper;
        this.userService=userService;
        this.userInfoRepo=userInfoRepo;
    }
    public LoginVo register(@Valid RegisterDTO registerDTO) throws IllegalAccessException{
        CommonUtil.trimObj(registerDTO);
        Specification<User> userSpecification= SearchUtil.eq(Constants.User.EMAIL,registerDTO.getEmail());
        if (userRepository.exists(userSpecification)){
            throw new IllegalArgumentException("existed email!");
        }
        User user=modelMapper.map(registerDTO,User.class);
        user.setRoleEnum(RoleEnum.USER);
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setStatus(UserStatusEnum.ACTIVE.getStatus());
        User userSaver=userRepository.save(user);
        var jwtToken=jwtService.generateToken(user);
        var freshToken=jwtService.generateRefreshToken(user);
        saveUserToken(userSaver,jwtToken);
        UserInfo in4=modelMapper.map(registerDTO,UserInfo.class);
        userInfoRepo.save(in4);
        return LoginVo.builder().
                accessToken(jwtToken).
                refreshToken(freshToken).
                build();
    }

    public LoginVo login(AuthenticationDTO dto) throws BusinessException{
        User user=userService.findByEmail(dto.getEmail());
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            throw new BusinessException(ExceptionEnum.INCORRECT_PASSWORD);
        }
        var jwtToken=jwtService.generateToken(user);
        var freshToken=jwtService.generateRefreshToken(user);
        revokeAllUserToken(user);
        saveUserToken(user,jwtToken);

        return LoginVo.builder()
                .refreshToken(freshToken)
                .accessToken(jwtToken)
                .build();
    }

    public LoginVo loginAdmin(AuthenticationDTO dto) throws BusinessException{
        User user=userService.findByEmail(dto.getEmail());
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            throw new BusinessException(ExceptionEnum.INCORRECT_PASSWORD);
        }
        if (!RoleEnum.ADMIN.equals(user.getRoleEnum())){
            throw new BusinessException(ExceptionEnum.NOT_PERMISSION);
        }
        var jwtToken=jwtService.generateToken(user);
        var freshToken=jwtService.generateRefreshToken(user);
        saveUserToken(user,jwtToken);
        revokeAllUserToken(user);
        return LoginVo.builder()
                .accessToken(jwtToken)
                .refreshToken(freshToken)
                .build();
    }
    public void revokeAllUserToken(User user){
        Specification<Token> spe=SearchUtil.eq(Constants.Token.USER_ID,user.getId());
        Specification<Token> speRevoked=SearchUtil.eq(Constants.Token.REVOKED,false);
        Specification<Token> speExpired=SearchUtil.eq(Constants.Token.EXPIRED,false);
        List<Token> validUserTokens=tokenRepository.findAll(spe.and(speRevoked.or(speExpired)));
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(s->{
            s.setRevoked(true);
            s.setExpired(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .userId(user.getId())
                .token(jwtToken)
                .tokenEnum(TokenEnum.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public LoginVo refreshToken(HttpServletRequest request, HttpServletResponse response) throws BusinessException, IOException {
        System.out.println("1122222");
        final String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        System.out.println(authHeader);
        if (authHeader== null || !authHeader.startsWith(Constants.BEARER+" ")){
            throw new BusinessException(ExceptionEnum.UNAUTHORIZED);
        }
        refreshToken=authHeader.substring(7);
        userEmail=jwtService.extractUsername(refreshToken);

        User user=userService.findByEmail(userEmail);
        if (jwtService.isTokenValid(refreshToken, user)) {
            var accessToken = jwtService.generateToken(user);
            revokeAllUserToken(user);
            saveUserToken(user, accessToken);
            var authResponse = LoginVo.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
            new ObjectMapper().writeValue(response.getOutputStream(), authResponse);

            LoginVo vo = new LoginVo();
            vo.setAccessToken(accessToken);
            vo.setRefreshToken(refreshToken);
            return vo;
        } else {
            throw new BusinessException(ExceptionEnum.UNAUTHORIZED);
        }
    }
}

package com.namtn.media.service.impl;

import com.namtn.media.common.Constants;
import com.namtn.media.core.auth.entity.User;
import com.namtn.media.core.auth.enumration.ExceptionEnum;
import com.namtn.media.core.auth.repository.UserRepository;
import com.namtn.media.core.auth.service.UserService;
import com.namtn.media.core.config.ThreadContext;
import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.CommonUtil;
import com.namtn.media.core.util.SearchUtil;
import com.namtn.media.entity.Follow;
import com.namtn.media.entity.UserInfo;
import com.namtn.media.model.main.dto.UserFollowSearch;
import com.namtn.media.model.main.dto.UserInfoDetailDto;
import com.namtn.media.model.main.dto.UserInfoSearchDto;
import com.namtn.media.model.main.vo.SimpleUserInfoVo;
import com.namtn.media.model.main.vo.UserInfoDetailVo;
import com.namtn.media.model.main.vo.UserInfoSearchVo;
import com.namtn.media.repository.FollowRepo;
import com.namtn.media.repository.UserInfoRepo;
import com.namtn.media.service.UserInfoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserInfoImpl implements UserInfoService {
    private final UserRepository userRepository;
    private final UserInfoRepo userInfoRepo;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final FollowRepo followRepo;
    @Autowired
    public UserInfoImpl(FollowRepo followRepo,UserService userService,UserRepository userRepository, UserInfoRepo userInfoRepo,ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userInfoRepo = userInfoRepo;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.followRepo = followRepo;
    }

    @Override
    public void updateInfo(UserInfoDetailDto userInfoDetailDto) throws BusinessException{
        String email= ThreadContext.getCurrentUser().getUsername();
        Specification<UserInfo> speUserInfo=SearchUtil.eq(Constants.User.EMAIL,email);
        //update user
        User user=userService.findByEmail(email);
        modelMapper.map(userInfoDetailDto,user);
        userRepository.save(user);

        //update user info
        UserInfo userInfo=userInfoRepo.findOne(speUserInfo).orElseThrow(()->new BusinessException(ExceptionEnum.EMAIL_NOT_FOUND));
        modelMapper.map(userInfoDetailDto,userInfo);
        userInfoRepo.save(userInfo);
    }

    @Override
    public UserInfoDetailVo getUserDetail(String email) throws BusinessException{
        User user=userService.findByEmail(email);
        UserInfoDetailVo vo=modelMapper.map(user,UserInfoDetailVo.class);

        String email1=ThreadContext.getCurrentUser().getUsername();
        Specification<UserInfo> speUserInfo=SearchUtil.eq(Constants.User.EMAIL,email);
        UserInfo userInfo=userInfoRepo.findOne(speUserInfo).orElseThrow(()-> new BusinessException(ExceptionEnum.EMAIL_NOT_FOUND));
        modelMapper.map(userInfo,vo);

        Specification<Follow> speFollowing=SearchUtil.eq(Constants.IMPACT,email);
        Specification<Follow> speFollower=SearchUtil.eq(Constants.TARGET,email);
        vo.setFollowing(followRepo.count(speFollowing));
        vo.setFollower(followRepo.count(speFollower));
        //check user current follow user?
        if (email.equals(email1)){
            vo.setFollow(false);
        } else {
            Specification<Follow> speImpact=SearchUtil.eq(Constants.IMPACT,email1);
            vo.setFollow(followRepo.exists(speFollower.and(speImpact)));
        }
        return vo;
    }

    @Override
    public int getAge(Date date) {
        if (date==null) return 0;
        Calendar birth=Calendar.getInstance();
        birth.setTime(date);
        Calendar today=Calendar.getInstance();
        int age=today.get(Calendar.YEAR)-birth.get(Calendar.YEAR);
        if ((today.get(Calendar.MONTH)<birth.get(Calendar.MONTH)) ||
                ((today.get(Calendar.MONTH)==birth.get(Calendar.MONTH)) && (today.get(Calendar.DAY_OF_MONTH)<birth.get(Calendar.DAY_OF_MONTH)))) {
            age--;}
        return age;
    }

    @Override
    public void follow(String email) {
        String userEmail=ThreadContext.getCurrentUser().getUsername();
        if (userEmail.equals(email)){
            return;
        }
        Specification<Follow> speImpact=SearchUtil.eq(Constants.IMPACT,userEmail);
        Specification<Follow> speTarget=SearchUtil.eq(Constants.TARGET,email);
        Optional<Follow> follow=followRepo.findOne(speTarget.and(speImpact));
        if (!follow.isPresent()){
            Follow follow1=Follow.builder()
                    .impact(userEmail)
                    .target(email)
                    .build();
            followRepo.save(follow1);
        } else {
            followRepo.delete(follow.get());
        }
    }

    @Override
    public List<UserInfoSearchVo> searchUser(UserInfoSearchDto userInfoSearchDto) {
        Specification<User> speSearch=SearchUtil.like(Constants.FULL_TEXT_SEARCH, CommonUtil.handleFullSearch(userInfoSearchDto.getKey()));
        List<User> users=userRepository.findAll(speSearch);
        List<String> emails=users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
        Specification<UserInfo> speUserInfo=SearchUtil.in(Constants.User.EMAIL,emails);
        List<UserInfo> userInfo=userInfoRepo.findAll(speUserInfo);
        Map<String,UserInfo> map=userInfo.stream()
                .collect(Collectors.toMap(UserInfo::getEmail, Function.identity()));
        return users.stream()
                .map(c->{
                    UserInfoSearchVo vo=UserInfoSearchVo.builder()
                            .avatar(map.get(c.getEmail()).getAvatar())
                            .firstName(c.getFirstName())
                            .lastName(c.getLastName())
                            .email(c.getEmail())
                            .build();
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public UserInfoDetailVo getMyProfile() throws BusinessException {
        String email=ThreadContext.getCurrentUser().getUsername();
        return getUserDetail(email);
    }

    @Override
    public SimpleUserInfoVo getSimpleInfo(String email) throws BusinessException {
        return userRepository.findSimpleUserInfoByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionEnum.EMAIL_NOT_FOUND));
    }

    @Override
    public Map<String, SimpleUserInfoVo> getSimpleInfo(List<String> emails) {
        Specification<User> speUser=SearchUtil.in(Constants.User.EMAIL,emails);
        Specification<UserInfo> speUserInfo=SearchUtil.in(Constants.User.EMAIL,emails);

        List<UserInfo> userInfo=userInfoRepo.findAll(speUserInfo);
        List<User> users=userRepository.findAll(speUser);

        Map<String,UserInfo> userInfoVo=new HashMap<>();
        userInfo.forEach(t->userInfoVo.put(t.getEmail(),t));
        Map<String,SimpleUserInfoVo> vo=new HashMap<>();
        users.forEach(t->{
            SimpleUserInfoVo infoVo=modelMapper.map(t,SimpleUserInfoVo.class);
            modelMapper.map(userInfoVo.get(t.getEmail()),infoVo);
            vo.put(t.getEmail(),infoVo);

        });
        return vo;
    }

    @Override
    public List<UserInfoSearchVo> searchFollowUser(UserFollowSearch req) throws BusinessException {
        String currentUserEmail = ThreadContext.getCurrentUser().getUsername();

        Specification<Follow> followSpec;
        boolean isFollower;
        switch (req.getType().toLowerCase()) {
            case "follower":
                followSpec = SearchUtil.eq(Constants.TARGET, currentUserEmail);
                isFollower = false;
                break;
            case "following":
                followSpec = SearchUtil.eq(Constants.IMPACT, currentUserEmail);
                isFollower = true;
                break;
            default:
                throw new BusinessException(ExceptionEnum.INVALID_INPUT);
        }
        List<Follow> follows = followRepo.findAll(followSpec);
        if (follows.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> relatedEmails = follows.stream()
                .map(isFollower ? Follow::getTarget : Follow::getImpact)
                .collect(Collectors.toList());

        if (relatedEmails.isEmpty()) {
            return Collections.emptyList();
        }

        Specification<User> userSpec = ((Specification<User>) (Specification<?>) SearchUtil.in(Constants.User.EMAIL, relatedEmails))
                .and((Specification<User>) (Specification<?>) SearchUtil.like(Constants.FULL_TEXT_SEARCH, CommonUtil.handleFullSearch(req.getKey())));

        List<User> users = userRepository.findAll(userSpec);
        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> userEmails = users.stream()
                .map(User::getEmail)
                .collect(Collectors.toList());

        Map<String, UserInfo> userInfoMap = userInfoRepo.findAll(SearchUtil.in(Constants.User.EMAIL, userEmails))
                .stream()
                .collect(Collectors.toMap(UserInfo::getEmail, Function.identity()));

        return users.stream()
                .map(user -> {
                    UserInfoSearchVo vo=modelMapper.map(user,UserInfoSearchVo.class);
                    UserInfo info = userInfoMap.get(user.getEmail());
                    if (info != null) {
                        vo.setAvatar(info.getAvatar());
                    }
                    return vo;
                })
                .collect(Collectors.toList());
    }

}

package com.namtn.media.service.impl;

import com.namtn.media.common.Constants;
import com.namtn.media.core.auth.entity.User;
import com.namtn.media.core.auth.repository.UserRepository;
import com.namtn.media.core.util.CommonUtil;
import com.namtn.media.core.util.SearchUtil;
import com.namtn.media.entity.Post;
import com.namtn.media.entity.UserInfo;
import com.namtn.media.model.main.dto.ManageUserDto;
import com.namtn.media.model.main.dto.StatisticalDto;
import com.namtn.media.model.main.vo.ManageUserVo;
import com.namtn.media.model.main.vo.StatisticalUserVo;
import com.namtn.media.model.main.vo.StatisticalVo;
import com.namtn.media.repository.PostRepo;
import com.namtn.media.repository.UserInfoRepo;
import com.namtn.media.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AdminImpl implements AdminService {
    private final UserRepository userRepository;
    private final PostRepo postRepo;
    private final UserInfoRepo userInfoRepo;
    private ModelMapper modelMapper;
    @Autowired
    public AdminImpl(ModelMapper modelMapper,UserInfoRepo userInfoRepo,UserRepository userRepository, PostRepo postRepo) {
        this.userRepository = userRepository;
        this.postRepo = postRepo;
        this.userInfoRepo = userInfoRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public StatisticalVo statistical(StatisticalDto statisticalDto) {
        StatisticalVo vo=new StatisticalVo();
        if (statisticalDto.getToDate()==null || statisticalDto.getFromDate()==null){
            return vo;
        }
        //total user and post
        vo.setTotalPost(postRepo.count());
        vo.setTotalUser(userRepository.count());
        //check condition of user and post:from
        Specification<UserInfo> speUser=SearchUtil.newSpecification();
        Specification<Post> spePost=SearchUtil.newSpecification();

        speUser=SearchUtil.ge(Constants.CREATED_DATE, CommonUtil.convertDateToLocalDateTime(statisticalDto.getFromDate()));
        spePost=SearchUtil.ge(Constants.CREATED_DATE, CommonUtil.convertDateToLocalDateTime(statisticalDto.getFromDate()));

        speUser=speUser.and(SearchUtil.le(Constants.CREATED_DATE, CommonUtil.convertDateToLocalDateTime(statisticalDto.getToDate())));
        spePost=spePost.and(SearchUtil.le(Constants.CREATED_DATE, CommonUtil.convertDateToLocalDateTime(statisticalDto.getToDate())));

        vo.setNewPost(postRepo.count(spePost));

        List<UserInfo> users=userInfoRepo.findAll(speUser);
//        System.out.println(users.size()+"hello ");
        Map<String,UserInfo> userMap=users.stream().collect(Collectors.toMap(UserInfo::getCreatedBy,Function.identity()));

        List<User> userList=userRepository.findAll(SearchUtil.in(Constants.User.EMAIL,userMap.keySet().stream().toList()));

        Map<String,StatisticalUserVo> voList=new HashMap<>();
        userList.forEach(user->{
            StatisticalUserVo userVo=modelMapper.map(user,StatisticalUserVo.class);
            userVo.setAvatar(userMap.get(user.getEmail())!=null?userMap.get(user.getEmail()).getAvatar():null);
            voList.put(user.getEmail(),userVo);
        });

        Specification<Post> speUserPost=SearchUtil.in(Constants.CREATED_BY,userList.stream().map(User::getEmail).collect(Collectors.toList()));
        List<Post> posts=postRepo.findAll(speUserPost);
        posts.forEach(post -> {
            StatisticalUserVo vo1 = voList.get(post.getCreatedBy());
            if (vo1 != null) {
                vo1.increaseTotalPost();
                if (post.isDeleted()) {
                    vo1.increasePostDeleted();
                }
            } else {
                System.err.println(" Không tìm thấy vo cho user: " + post.getCreatedBy());
            }
        });
        vo.setNewUser(voList.values().stream().collect(Collectors.toList()));
        return vo;
    }

    @Override
    public List<ManageUserVo> managerUser(ManageUserDto request) {
        Map<String, UserInfo> userMap = userInfoRepo
                .findAll()
                .stream()
                .collect(Collectors.toMap(UserInfo::getEmail, user -> user));
        List<User> users = userRepository.findAll();
        List<ManageUserVo> vo = new ArrayList<>();
        users.forEach(user -> {
            ManageUserVo u = modelMapper.map(user, ManageUserVo.class);
            modelMapper.map(userMap.get(user.getEmail()), u);
            vo.add(u);
        });
        return vo;
    }
}

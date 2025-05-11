package com.namtn.media.core.auth.service;

import com.namtn.media.common.Constants;
import com.namtn.media.core.auth.entity.User;
import com.namtn.media.core.auth.enumration.ExceptionEnum;
import com.namtn.media.core.auth.model.dto.ChangePasswordDTO;
import com.namtn.media.core.auth.repository.UserRepository;
import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.SearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    @Autowired
    public UserService(PasswordEncoder passwordEncoder,UserRepository userRepository){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public User findByEmail(String email) throws BusinessException {
        Specification<User> userSpecification = SearchUtil.eq(Constants.User.EMAIL, email);
        Optional<User> optionalUser = userRepository.findOne(userSpecification);
        return optionalUser.orElseThrow(() -> new BusinessException(ExceptionEnum.EMAIL_NOT_FOUND));
    }


    public User findById(Long id) throws BusinessException{
        User user=userRepository.findById(id).orElseThrow(() -> new BusinessException(ExceptionEnum.EMAIL_NOT_FOUND));
        return user;
    }

    public void changePassword(ChangePasswordDTO dto, Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principalObj = authentication.getPrincipal();

        if (!(principalObj instanceof User user)) {
            throw new IllegalStateException("Authentication principal is not a valid User instance.");
        }

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new IllegalStateException("Password not correct");
        }

        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalStateException("Passwords are not the same");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}

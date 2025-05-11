package com.namtn.media.core.auth.service;

import com.namtn.media.core.auth.repository.TokenRepository;
import com.namtn.media.core.model.BusinessException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = BusinessException.class)
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(jakarta.servlet.http.HttpServletRequest request,
                       jakarta.servlet.http.HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer") || authHeader.equals("")){
            throw new RuntimeException("Unauthorized");
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt);
        if (storedToken!=null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }

    public boolean logoutCustom(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String jwt;
        if (authHeader==null || !authHeader.startsWith("Bearer") || authHeader.equals("")){
            throw new RuntimeException("Unauthorized");
        }
        jwt=authHeader.substring(7);
        var storedToken=tokenRepository.findByToken(jwt);
        if (storedToken!=null){
            storedToken.setRevoked(true);
            storedToken.setExpired(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        } else {
            throw new RuntimeException("Token is expired or revoked");
        }
        return true;
    }
}

package com.namtn.media.core.auth.repository;

import com.namtn.media.core.auth.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token,Long> , JpaSpecificationExecutor<Token> {
    Token findByToken(String token);
}

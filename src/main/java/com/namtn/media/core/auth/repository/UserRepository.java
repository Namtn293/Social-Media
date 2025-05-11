package com.namtn.media.core.auth.repository;

import com.namtn.media.core.auth.entity.User;
import com.namtn.media.model.main.vo.SimpleUserInfoVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
        @Query("SELECT new com.namtn.media.model.main.vo.SimpleUserInfoVo(u.email, ui.avatar, u.firstName, u.lastName) " +
                "FROM User u JOIN UserInfo ui ON u.email = ui.email " +
                "WHERE u.email = :email")
        Optional<SimpleUserInfoVo> findSimpleUserInfoByEmail(@Param("email") String email);
}

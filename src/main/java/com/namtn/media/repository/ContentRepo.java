package com.namtn.media.repository;

import com.namtn.media.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentRepo extends JpaRepository<Content,Long>, JpaSpecificationExecutor<Content> {

    @Query(value = "select * from main_content" +
            " where post_id=:postId"+
            " limit 1",nativeQuery = true)
    Optional<Content> findTheFirstContent(@Param("postId")Long postId);
}

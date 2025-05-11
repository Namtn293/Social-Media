package com.namtn.media.repository;

import com.namtn.media.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepo extends JpaRepository<Follow,Long>, JpaSpecificationExecutor<Follow> {
}

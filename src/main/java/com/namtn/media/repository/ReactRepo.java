package com.namtn.media.repository;

import com.namtn.media.entity.React;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactRepo extends JpaRepository<React,Long>, JpaSpecificationExecutor<React> {
    @Query(value = "select r.ref,count(r.ref) as react_count"+
                    " from main_react as r"+
                    " group by r.ref"+
                    " order by react_count desc"+
                    " limit 10",nativeQuery = true)
    List<Object[]> getTop10Trending();
}

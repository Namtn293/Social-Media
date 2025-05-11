package com.namtn.media.service;

import com.namtn.media.entity.History;
import com.namtn.media.model.main.vo.HistoryVo;

import java.util.List;

public interface HistoryService {
    void create(History history);
    List<HistoryVo> getAllHistory();
}

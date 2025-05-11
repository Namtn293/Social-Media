package com.namtn.media.service.impl;

import com.namtn.media.common.Constants;
import com.namtn.media.core.config.ThreadContext;
import com.namtn.media.core.util.SearchUtil;
import com.namtn.media.entity.History;
import com.namtn.media.model.main.vo.HistoryVo;
import com.namtn.media.repository.HistoryRepo;
import com.namtn.media.service.HistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class HistoryImpl implements HistoryService {
    private final HistoryRepo historyRepo;
    private final ModelMapper modelMapper;
    @Autowired
    public HistoryImpl(HistoryRepo historyRepo, ModelMapper modelMapper) {
        this.historyRepo = historyRepo;
        this.modelMapper = modelMapper;
    }
    @Override
    public void create(History history) {
        historyRepo.save(history);
    }
    @Override
    public List<HistoryVo> getAllHistory() {
        List<HistoryVo> historyVos=new ArrayList<>();
        Specification<History> spe= SearchUtil.eq(Constants.CREATED_BY, ThreadContext.getCurrentUser().getUsername());
        List<History> histories=historyRepo.findAll(spe);
        histories.forEach(h->{
            HistoryVo vo=modelMapper.map(h,HistoryVo.class);
            historyVos.add(vo);
        });
        return historyVos;
    }
}

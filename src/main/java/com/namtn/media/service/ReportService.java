package com.namtn.media.service;

import com.namtn.media.core.model.BusinessException;
import com.namtn.media.entity.Report;
import com.namtn.media.model.main.dto.HandleReportDto;
import com.namtn.media.model.main.dto.ReportDto;
import com.namtn.media.model.main.vo.ReportDetailVo;
import com.namtn.media.model.main.vo.ReportVo;

import java.util.List;

public interface ReportService {
    List<ReportVo> getListReport() throws BusinessException;
    ReportDetailVo getDetailReport(Long id) throws BusinessException;
    Report findById(Long id) throws BusinessException;
    void handleReport(HandleReportDto handleReportDto)throws BusinessException;
}

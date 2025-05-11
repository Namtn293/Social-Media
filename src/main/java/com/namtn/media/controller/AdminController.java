package com.namtn.media.controller;

import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.ResponseUtil;
import com.namtn.media.core.util.SuccessResponse;
import com.namtn.media.model.main.dto.StatisticalDto;
import com.namtn.media.model.main.vo.StatisticalVo;
import com.namtn.media.service.AdminService;
import com.namtn.media.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/admin")
@RestController
public class AdminController {
    private final AdminService adminService;
    private final ReportService reportService;
    @Autowired
    public AdminController(AdminService adminService, ReportService reportService) {
        this.adminService = adminService;
        this.reportService = reportService;
    }

    @PostMapping("/statistical")
    public SuccessResponse<StatisticalVo> statistical(@RequestBody StatisticalDto dto) throws BusinessException,IllegalAccessException{
        return ResponseUtil.ok(adminService.statistical(dto));
    }
}

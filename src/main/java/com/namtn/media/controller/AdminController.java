package com.namtn.media.controller;

import com.namtn.media.core.model.BusinessException;
import com.namtn.media.core.util.ResponseUtil;
import com.namtn.media.core.util.SuccessResponse;
import com.namtn.media.model.main.dto.*;
import com.namtn.media.model.main.vo.ManageUserVo;
import com.namtn.media.model.main.vo.ReportDetailVo;
import com.namtn.media.model.main.vo.ReportVo;
import com.namtn.media.model.main.vo.StatisticalVo;
import com.namtn.media.service.AdminService;
import com.namtn.media.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/manage-user")
    public SuccessResponse<List<ManageUserVo>> manage(@RequestBody ManageUserDto dto){
        return ResponseUtil.ok(adminService.managerUser(dto));
    }

    @GetMapping("/report")
    public SuccessResponse<List<ReportVo>> getReport() throws BusinessException{
        return ResponseUtil.ok(reportService.getListReport());
    }

    @PostMapping("/report")
    public SuccessResponse<ReportDetailVo> getDetailReport(@RequestBody SimpleIdDto id) throws BusinessException{
        return ResponseUtil.ok(reportService.getDetailReport(id.getId()));
    }

    @PostMapping("/report/handle")
    public SuccessResponse<String> handleReport(@RequestBody HandleReportDto dto) throws BusinessException{
        reportService.handleReport(dto);
        return ResponseUtil.ok("Success");
    }
}

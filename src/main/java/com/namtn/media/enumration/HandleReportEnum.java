package com.namtn.media.enumration;

import com.namtn.media.core.auth.enumration.ExceptionEnum;
import com.namtn.media.core.model.BusinessException;

public enum HandleReportEnum {
    A("A", "Active"),
    D("D", "Delete"),
    ;
    private String code;
    private String action;

    HandleReportEnum(String code, String action) {
        this.code = code;
        this.action = action;
    }

    public static HandleReportEnum getByCode(String code) throws BusinessException {
        for (HandleReportEnum value : HandleReportEnum.values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        throw new BusinessException(ExceptionEnum.INVALID_REPORT_TYPE);
    }
}

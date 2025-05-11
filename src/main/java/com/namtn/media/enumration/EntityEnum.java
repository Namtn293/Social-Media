package com.namtn.media.enumration;

import com.namtn.media.core.auth.enumration.ExceptionEnum;
import com.namtn.media.core.model.BusinessException;

public enum EntityEnum {
    POST("post"),
    COMMENT("comment"),
    MESSAGE("message"),
    AVATAR("avatar"),
    COVER("cover");
    private final String name;
    public static EntityEnum getByName(String name) throws BusinessException {
        for (EntityEnum value : EntityEnum.values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        throw new BusinessException(ExceptionEnum.INVALID_REPORT_TYPE);
    }
    EntityEnum(String name){this.name=name;}
}

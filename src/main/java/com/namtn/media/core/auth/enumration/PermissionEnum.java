package com.namtn.media.core.auth.enumration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public enum PermissionEnum {
    ADMIN_READ("admin:read"),
    ADMIN_CREATE("admin:read"),
    ADMIN_DELETE("admin:read"),
    ADMIN_UPDATE("admin:read"),
    MANAGER_READ("management:read"),
    MANAGER_UPDATE("management:update"),
    MANAGER_CREATE("management:create"),
    MANAGER_DELETE("management:delete");

    @Getter
    public final String permission;

    PermissionEnum(String permission){
        this.permission=permission;
    }
}

package com.namtn.media.core.auth.enumration;

import lombok.Getter;


import java.util.Collections;
import java.util.Set;


public enum RoleEnum {

    USER(Collections.emptySet()),
    ADMIN(Set.of(
            PermissionEnum.ADMIN_READ,
            PermissionEnum.ADMIN_CREATE,
            PermissionEnum.ADMIN_DELETE,
            PermissionEnum.ADMIN_UPDATE,
            PermissionEnum.MANAGER_READ,
            PermissionEnum.MANAGER_CREATE,
            PermissionEnum.MANAGER_DELETE,
            PermissionEnum.MANAGER_UPDATE
    )),
    MANAGER(Set.of(
            PermissionEnum.MANAGER_CREATE,
            PermissionEnum.MANAGER_READ,
            PermissionEnum.MANAGER_DELETE,
            PermissionEnum.MANAGER_UPDATE
    ));
    @Getter
    private final Set<PermissionEnum> permissionEnums;

    RoleEnum(Set<PermissionEnum> permissionEnums){
        this.permissionEnums=permissionEnums;
    }
}

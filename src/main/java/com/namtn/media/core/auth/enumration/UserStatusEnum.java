package com.namtn.media.core.auth.enumration;

import lombok.Getter;

public enum UserStatusEnum {
    ACTIVE("A", "Account is active"),
    LOCK("L", "Account was locked"),
    VERIFYING("V", "Account is verifying");
    String status;
    String description;
    UserStatusEnum(String status,String description){
        this.status=status;
        this.description=description;
    }

    public String getStatus() {
        return status;
    }
}

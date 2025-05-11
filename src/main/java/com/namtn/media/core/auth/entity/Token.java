package com.namtn.media.core.auth.entity;

import com.namtn.media.core.auth.enumration.TokenEnum;
import com.namtn.media.core.util.EntityBase;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.java.Log;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "AUTH_TOKEN")
public class Token extends EntityBase {
    @Column(unique = true,name = "TOKEN")
    public String token;
    @Column(name = "TOKEN_TYPE")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    public TokenEnum tokenEnum=TokenEnum.BEARER;
    @Column(name = "REVOKED")
    public boolean revoked;

    @Column(name = "EXPIRED")
    public boolean expired;

    @Column(name = "USER_ID")
    public Long userId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenEnum getTokenEnum() {
        return tokenEnum;
    }

    public void setTokenEnum(TokenEnum tokenEnum) {
        this.tokenEnum = tokenEnum;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

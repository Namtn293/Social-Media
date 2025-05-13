package com.namtn.media.core.auth.entity;

import com.namtn.media.core.auth.enumration.RoleEnum;
import com.namtn.media.core.util.CommonUtil;
import com.namtn.media.core.util.EntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "AUTH_USER")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User extends EntityBase implements UserDetails {
    @Column(name = "FIRST_NAME",nullable = false)
    private String firstName;
    @Column(name = "LAST_NAME",nullable = false)
    private String lastName;
    @Column(name = "EMAIL",unique = true,nullable = false)
    private String email;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleEnum;
    @Column(name = "FULL_TEXT_SEARCH")
    private String fullTextSearch;
    @Column(name = "PASSWORD", nullable = false)
    private String password;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @PrePersist
    public void OnInsert(){
        super.onInsert();
        StringBuilder str= new StringBuilder();
        str.append(email);
        str.append(firstName);
        str.append(lastName);
        this.fullTextSearch= CommonUtil.handleFullSearch(str.toString());
    }

    @PreUpdate
    public void OnUpdate(){
        super.onUpdate();
        StringBuilder str= new StringBuilder();
        str.append(email);
        str.append(firstName);
        str.append(lastName);
        this.fullTextSearch= CommonUtil.handleFullSearch(str.toString());
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RoleEnum getRoleEnum() {
        return roleEnum;
    }

    public void setRoleEnum(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
    }

    public String getFullTextSearch() {
        return fullTextSearch;
    }

    public void setFullTextSearch(String fullTextSearch) {
        this.fullTextSearch = fullTextSearch;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

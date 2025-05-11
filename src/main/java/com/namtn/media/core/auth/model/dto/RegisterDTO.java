package com.namtn.media.core.auth.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class RegisterDTO {
    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    @NotNull
    private Date birth;
    private String address;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

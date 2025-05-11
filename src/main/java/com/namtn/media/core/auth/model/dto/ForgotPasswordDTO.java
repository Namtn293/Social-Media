package com.namtn.media.core.auth.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForgotPasswordDTO {
    @NotEmpty
    @NotNull
    private String email;

    @NotEmpty
    @NotNull
    private String phoneNumber;
}

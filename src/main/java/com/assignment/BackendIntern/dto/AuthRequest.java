package com.assignment.BackendIntern.dto;

import com.assignment.BackendIntern.constant.AppConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Login request body")
public class AuthRequest {

    @NotBlank(message = AppConstants.EMAIL_REQUIRED)
    @Email(message = AppConstants.EMAIL_INVALID)
    @Schema(description = "Email address", example = "john@example.com")
    private String email;

    @NotBlank(message = AppConstants.PASSWORD_REQUIRED)
    @Schema(description = "Password", example = "password123")
    private String password;
}
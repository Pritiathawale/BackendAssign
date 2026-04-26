package com.assignment.BackendIntern.dto;

import com.assignment.BackendIntern.constant.AppConstants;
import com.assignment.BackendIntern.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Register request body")
public class RegisterRequest {

    @NotBlank(message = AppConstants.NAME_REQUIRED)
    @Size(min = 2, max = 50, message = AppConstants.NAME_SIZE)
    @Schema(description = "Full name", example = "John Doe")
    private String name;

    @NotBlank(message = AppConstants.EMAIL_REQUIRED)
    @Email(message = AppConstants.EMAIL_INVALID)
    @Schema(description = "Email address", example = "john@example.com")
    private String email;

    @NotBlank(message = AppConstants.PASSWORD_REQUIRED)
    @Size(min = 6, message = AppConstants.PASSWORD_SIZE)
    @Schema(description = "Password (min 6 chars)", example = "password123")
    private String password;

    @Schema(description = "Role (optional, defaults to USER)", example = "USER")
    private Role role;
}
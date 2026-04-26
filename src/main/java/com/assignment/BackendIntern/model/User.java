package com.assignment.BackendIntern.model;

import com.assignment.BackendIntern.constant.AppConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Schema(description = "User entity")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "User ID", example = "1")
    private Long id;

    @NotBlank(message = AppConstants.NAME_REQUIRED)
    @Size(min = 2, max = 50, message = AppConstants.NAME_SIZE)
    @Schema(description = "Full name", example = "John Doe")
    private String name;

    @NotBlank(message = AppConstants.EMAIL_REQUIRED)
    @Email(message = AppConstants.EMAIL_INVALID)
    @Column(unique = true, nullable = false)
    @Schema(description = "Email address", example = "john@example.com")
    private String email;

    @NotBlank(message = AppConstants.PASSWORD_REQUIRED)
    @Size(min = 6, message = AppConstants.PASSWORD_SIZE)
    @JsonIgnore                               // ✅ hide password from API responses
    @Schema(description = "Password", example = "password123")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "User role", example = "USER",
            allowableValues = {"USER", "ADMIN"})
    private Role role = Role.USER;
}
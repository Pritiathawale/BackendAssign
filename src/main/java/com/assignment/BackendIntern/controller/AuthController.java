package com.assignment.BackendIntern.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.BackendIntern.dto.AuthRequest;
import com.assignment.BackendIntern.dto.AuthResponse;
import com.assignment.BackendIntern.dto.RegisterRequest;
import com.assignment.BackendIntern.service.AuthService;
import com.assignment.BackendIntern.service.TokenCacheService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "APIs for registration and login")
public class AuthController {

    private final AuthService authService;
    @Autowired private TokenCacheService tokenCacheService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Validation failed"),
        @ApiResponse(responseCode = "409", description = "User already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
    	log.info("Registering new user with email: {}", request.getEmail());
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "Login and get JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful — returns JWT token"),
        @ApiResponse(responseCode = "401", description = "Invalid email or password"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
    	log.info("Login attempt for email: {}", request.getEmail());
        return ResponseEntity.ok(authService.login(request));
    }
    
    @Operation(summary = "Logout and invalidate token")
    @ApiResponse(responseCode = "200", description = "Logged out successfully")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();
            tokenCacheService.blacklistToken(token);
            log.info("User logged out successfully");
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.badRequest().body("Invalid token");
    }

    @Operation(summary = "Test authenticated access")
    @ApiResponse(responseCode = "200", description = "Returns logged in username")
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Working! User: " + auth.getName());
    }
}
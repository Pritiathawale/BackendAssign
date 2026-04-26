package com.assignment.BackendIntern.security;

import com.assignment.BackendIntern.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JwtAuth implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now().toString(),
                401,
                "Unauthorized: Token missing or invalid",
                request.getRequestURI()
        );

        try {
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
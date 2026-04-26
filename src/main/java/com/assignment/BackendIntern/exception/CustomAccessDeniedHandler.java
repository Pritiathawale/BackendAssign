package com.assignment.BackendIntern.exception;

import com.assignment.BackendIntern.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now().toString(),
                403,
                "Forbidden: You don't have permission",
                request.getRequestURI()
        );

        try {
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
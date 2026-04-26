package com.assignment.BackendIntern.security;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.assignment.BackendIntern.constant.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired private JwtUtil jwtUtil;
    @Autowired private CustomUserDetailsService userDetailsService;
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith(AppConstants.AUTH_PATH)    ||
               path.startsWith(AppConstants.SWAGGER_PATH) ||
               path.startsWith(AppConstants.DOCS_PATH)    ||
               path.equals(AppConstants.SWAGGER_HTML);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

       
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, request, "Token missing or invalid format");
            return;
        }

        try {
          
            token = authHeader.substring(7).trim();
            username = jwtUtil.extractUsername(token);
        } catch (Exception e) {
            sendErrorResponse(response, request, "Invalid or malformed token");
            return;
        }

        try {
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                } else {
                    sendErrorResponse(response, request, "Token validation failed");
                    return;
                }
            }
        } catch (Exception e) {
            sendErrorResponse(response, request, "Authentication error");
            return;
        }

        filterChain.doFilter(request, response);
    }
    
    
    
    private void sendErrorResponse(HttpServletResponse response,
	            HttpServletRequest request,
	            String message) throws IOException {
	response.setContentType("application/json");
	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	
	Map<String, Object> error = new HashMap<>();
	error.put("timestamp", LocalDateTime.now().toString());
	error.put("status", 401);
	error.put("error", message);
	error.put("path", request.getRequestURI());
	
	new ObjectMapper().writeValue(response.getOutputStream(), error);
	}
}




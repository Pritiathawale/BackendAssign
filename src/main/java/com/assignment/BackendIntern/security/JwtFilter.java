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
import com.assignment.BackendIntern.service.TokenCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired private JwtUtil jwtUtil;
    @Autowired private CustomUserDetailsService userDetailsService;
    
    @Autowired private TokenCacheService tokenCacheService; 
    
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
        	 log.warn("No JWT token in request: {}", request.getRequestURI());

            sendErrorResponse(response, request, "Token missing or invalid format");
            return;
        }

        try {
          
            token = authHeader.substring(7).trim();
            
            //check if token is blacklisted
            if (tokenCacheService.isTokenBlacklisted(token)) {
                log.warn("Blacklisted token used for: {}", request.getRequestURI());
                sendErrorResponse(response, request, "Token has been invalidated. Please login again.");
                return;
            }
            
            username = jwtUtil.extractUsername(token);
            log.info("JWT token extracted for user: {}", username);
        } catch (Exception e) {
        	log.error("Invalid JWT token: {}", e.getMessage());
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
                    log.info("User authenticated successfully: {}", username);

                } else {
                	  log.warn("JWT token validation failed for user: {}", username);

                    sendErrorResponse(response, request, "Token validation failed");
                    return;
                }
            }
        } catch (Exception e) {
        	 log.error("Authentication error for user {}: {}", username, e.getMessage());

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




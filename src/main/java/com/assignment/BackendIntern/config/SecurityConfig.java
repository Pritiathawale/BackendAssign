package com.assignment.BackendIntern.config;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.assignment.BackendIntern.constant.AppConstants;
import com.assignment.BackendIntern.exception.CustomAccessDeniedHandler;
import com.assignment.BackendIntern.security.CustomUserDetailsService;
import com.assignment.BackendIntern.security.JwtAuth;
import com.assignment.BackendIntern.security.JwtFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired private JwtFilter jwtFilter;
    @Autowired private CustomUserDetailsService userDetailsService;
    private JwtAuth jwtAuth;
    @Autowired private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            
            .authorizeHttpRequests(auth -> auth
            	    .requestMatchers(AppConstants.AUTH_BASE + "/**").permitAll()
            	    .requestMatchers(AppConstants.ADMIN_BASE + "/**").hasRole(AppConstants.ROLE_ADMIN)
            	    .requestMatchers(AppConstants.TASK_BASE + "/**").hasAnyRole(AppConstants.ROLE_USER, AppConstants.ROLE_ADMIN)
            	    .requestMatchers(AppConstants.SWAGGER_UI, AppConstants.SWAGGER_DOCS, AppConstants.SWAGGER_HTML).permitAll()
            	    .anyRequest().authenticated()
            	)
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint(jwtAuth)  
                    .accessDeniedHandler(accessDeniedHandler) 
                )
            .sessionManagement(sess -> sess
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // ✅ no sessions
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // ✅

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}


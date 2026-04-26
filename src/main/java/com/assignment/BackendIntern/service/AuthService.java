package com.assignment.BackendIntern.service;



import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.assignment.BackendIntern.constant.AppConstants;
import com.assignment.BackendIntern.dto.AuthRequest;
import com.assignment.BackendIntern.dto.AuthResponse;
import com.assignment.BackendIntern.dto.RegisterRequest;
import com.assignment.BackendIntern.exception.ResourceNotFoundException;
import com.assignment.BackendIntern.exception.UnauthorizedException;
import com.assignment.BackendIntern.exception.UserAlreadyExistsException;
import com.assignment.BackendIntern.model.User;
import com.assignment.BackendIntern.repository.UserRepository;
import com.assignment.BackendIntern.security.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

 
    public String register(RegisterRequest request) {
    	 log.info("Register attempt for email: {}", request.getEmail());

    	  if (userRepository.findByEmail(request.getEmail()).isPresent()) {
    		  log.warn("Registration failed — email already exists: {}", request.getEmail());
              throw new UserAlreadyExistsException(AppConstants.USER_ALREADY_EXISTS);
          }
    	  
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        log.info("User registered successfully with email: {}", request.getEmail());

        return AppConstants.REGISTER_SUCCESS;
    }


    public AuthResponse login(AuthRequest request) {
    	 log.info("Login attempt for email: {}", request.getEmail());


        User user = userRepository.findByEmail(request.getEmail())
        		.orElseThrow(() -> {
                    log.warn("Login failed — user not found: {}", request.getEmail());
                    return new ResourceNotFoundException(AppConstants.USER_NOT_FOUND);
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        	  log.warn("Login failed — invalid password for email: {}", request.getEmail());
              
        	throw new UnauthorizedException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}
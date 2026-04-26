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

@Service
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

    	  if (userRepository.findByEmail(request.getEmail()).isPresent()) {
              throw new UserAlreadyExistsException(AppConstants.USER_ALREADY_EXISTS);
          }
    	  
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);

        return AppConstants.REGISTER_SUCCESS;
    }


    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        	throw new UnauthorizedException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}
package com.assignment.BackendIntern.security;


import com.assignment.BackendIntern.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.assignment.BackendIntern.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        return userRepository.findByEmail(email)
//                .map(user -> new CustomUserDetails(user))
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email) // ✅ match the subject
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
       
        return new CustomUserDetails(user);
    }
}    

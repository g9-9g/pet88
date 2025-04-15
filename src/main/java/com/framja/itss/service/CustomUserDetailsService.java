package com.framja.itss.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, org.springframework.security.core.userdetails.User> users = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public CustomUserDetailsService() {
        // Create some test users
        users.put("admin", new org.springframework.security.core.userdetails.User(
            "admin",
            passwordEncoder.encode("admin123"),
            new ArrayList<>()
        ));
        
        users.put("user", new org.springframework.security.core.userdetails.User(
            "user",
            passwordEncoder.encode("user123"),
            new ArrayList<>()
        ));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.get(username);
    }
} 
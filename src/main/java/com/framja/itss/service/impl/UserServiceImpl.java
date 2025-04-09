package com.framja.itss.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.framja.itss.model.User;
import com.framja.itss.service.JwtService;
import com.framja.itss.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private Long nextId = 1L;

    @Autowired
    private JwtService jwtService;

    @Override
    public User register(String username, String password) {
        User user = new User(nextId++, username, password);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public String login(String username, String password) {
        User user = users.values().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
        
        if (user != null) {
            return jwtService.generateToken(user);
        }
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public boolean lockUser(Long id) {
        User user = users.get(id);
        if (user != null) {
            user.setLocked(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean unlockUser(Long id) {
        User user = users.get(id);
        if (user != null) {
            user.setLocked(false);
            return true;
        }
        return false;
    }

    @Override
    public User updateProfile(Long id, String username, String email, String fullName, String password) {
        User user = users.get(id);
        if (user != null) {
            if (username != null && !username.isEmpty()) {
                user.setUsername(username);
            }
            if (email != null && !email.isEmpty()) {
                user.setEmail(email);
            }
            if (fullName != null && !fullName.isEmpty()) {
                user.setFullName(fullName);
            }
            if (password != null && !password.isEmpty()) {
                user.setPassword(password);
            }
            return user;
        }
        return null;
    }
} 
package com.framja.itss.service;

import com.framja.itss.model.User;

public interface UserService {
    User register(String username, String password);
    String login(String username, String password);
    User getUserById(Long id);
    boolean lockUser(Long id);
    boolean unlockUser(Long id);
    User updateProfile(Long id, String username, String email, String fullName, String password);
} 
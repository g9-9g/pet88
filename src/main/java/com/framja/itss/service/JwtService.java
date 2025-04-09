package com.framja.itss.service;

import com.framja.itss.model.User;

public interface JwtService {
    String generateToken(User user);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
} 
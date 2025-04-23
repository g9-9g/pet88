package com.framja.itss.service;

import com.framja.itss.dto.AuthRequest;
import com.framja.itss.dto.AuthResponse;
import com.framja.itss.dto.RegisterRequest;

public interface AuthenticationService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
} 
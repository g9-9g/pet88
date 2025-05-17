package com.framja.itss.auth.service;

import com.framja.itss.auth.dto.AuthRequest;
import com.framja.itss.auth.dto.AuthResponse;
import com.framja.itss.auth.dto.RegisterRequest;

public interface AuthenticationService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
} 
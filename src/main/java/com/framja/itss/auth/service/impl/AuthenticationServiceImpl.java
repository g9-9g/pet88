package com.framja.itss.auth.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.framja.itss.auth.dto.AuthRequest;
import com.framja.itss.auth.dto.AuthResponse;
import com.framja.itss.auth.dto.RegisterRequest;
import com.framja.itss.auth.security.JwtTokenProvider;
import com.framja.itss.auth.service.AuthenticationService;
import com.framja.itss.common.service.UserQueryService;
import com.framja.itss.users.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserQueryService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Kiểm tra nếu username đã tồn tại
        if (userService.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã được sử dụng");
        }
        
        // Kiểm tra nếu email đã tồn tại và không rỗng
        if (request.getEmail() != null && !request.getEmail().isEmpty() 
                && userService.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng");
        }
        
        User savedUser = userService.saveUser(
            request.getUsername(),
            request.getPassword(),
            request.getEmail(),
            request.getFullName(),
            request.getRole()
        );
        
        var jwtToken = jwtTokenProvider.generateToken(savedUser);
        
        return AuthResponse.builder()
                .token(jwtToken)
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .role(savedUser.getRole())
                .build();
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Tên đăng nhập hoặc mật khẩu không đúng");
        }
        
        var user = userService.getUserByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        
        var jwtToken = jwtTokenProvider.generateToken(user);
        
        return AuthResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }
} 
package com.framja.itss.users.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.framja.itss.auth.security.JwtTokenProvider;
import com.framja.itss.common.enums.RoleName;
import com.framja.itss.common.service.UserQueryService;
import com.framja.itss.users.dto.ChangePasswordRequest;
import com.framja.itss.users.dto.UserDto;
import com.framja.itss.users.entity.User;
import com.framja.itss.users.repository.UserRepository;
import com.framja.itss.users.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserQueryService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .locked(user.isLocked())
                .build();
    }

    @Override
    public User convertToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .fullName(userDto.getFullName())
                .locked(userDto.isLocked())
                .build();
    }

    @Override
    @Transactional
    public String changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return jwtTokenProvider.generateToken(user);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    @Override
    public User saveUser(String username, String password, String email, String fullName, RoleName role) {
        var user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .fullName(fullName)
                .locked(false)
                .role(role)
                .build();
        
        return userRepository.save(user);
    }

    @Override
    public List<User> searchUsersByUsername(String username, RoleName role) {
        List<User> users;
        
        if (role != null) {
            users = userRepository.findByRole(role);
        } else {
            users = userRepository.findAll();
        }
        
        if (username != null && !username.isEmpty()) {
            return users.stream()
                    .filter(user -> user.getUsername().toLowerCase().contains(username.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        return users;
    }
} 
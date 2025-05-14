package com.framja.itss.users.service;

import java.util.List;
import java.util.Optional;

import com.framja.itss.auth.dto.ChangePasswordRequest;
import com.framja.itss.users.dto.UserDto;
import com.framja.itss.users.entity.User;

public interface UserService {
    User createUser(User user);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    List<User> getAllUsers();
    User updateUser(User user);
    void deleteUser(Long id);
    UserDto convertToDto(User user);
    User convertToEntity(UserDto userDto);
    String changePassword(String username, ChangePasswordRequest request);
} 
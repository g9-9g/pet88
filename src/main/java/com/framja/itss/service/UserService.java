package com.framja.itss.service;

import com.framja.itss.dto.UserDto;
import com.framja.itss.entity.User;
import com.framja.itss.dto.ChangePasswordRequest;

import java.util.List;
import java.util.Optional;

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
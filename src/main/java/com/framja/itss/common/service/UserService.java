package com.framja.itss.common.service;

import java.util.Optional;

import com.framja.itss.auth.dto.ChangePasswordRequest;
import com.framja.itss.users.dto.UserDto;
import com.framja.itss.users.entity.User;

public interface UserService {
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    UserDto convertToDto(User user);
    User convertToEntity(UserDto userDto);
    String changePassword(String username, ChangePasswordRequest request);
}

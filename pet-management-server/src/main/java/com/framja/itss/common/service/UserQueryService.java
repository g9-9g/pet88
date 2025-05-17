package com.framja.itss.common.service;

import java.util.List;
import java.util.Optional;

import com.framja.itss.common.enums.RoleName;
import com.framja.itss.users.dto.ChangePasswordRequest;
import com.framja.itss.users.dto.UserDto;
import com.framja.itss.users.entity.User;

public interface UserQueryService {
    Optional<User> getUserById(Long id);
    Optional<User> getUserByUsername(String username);
    UserDto convertToDto(User user);
    User convertToEntity(UserDto userDto);
    String changePassword(String username, ChangePasswordRequest request);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User saveUser(String username, String password, String email, String fullName, RoleName role);
}

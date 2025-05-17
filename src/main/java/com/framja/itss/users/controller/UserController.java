package com.framja.itss.users.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framja.itss.common.enums.RoleName;
import com.framja.itss.users.dto.ChangePasswordRequest;
import com.framja.itss.users.dto.UserDto;
import com.framja.itss.users.entity.User;
import com.framja.itss.users.service.UserService;

import com.framja.itss.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(userService.convertToDto(createdUser), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        System.out.println("Getting user by ID: " + id);
        return userService.getUserById(id)
                .map(user -> new ResponseEntity<>(userService.convertToDto(user), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers().stream()
                .map(userService::convertToDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        if (!id.equals(userDto.getId())) {
            throw new ResourceNotFoundException("User ID mismatch");
        }
        
        return userService.getUserById(id)
                .map(existingUser -> {
                    User updatedUser = userService.convertToEntity(userDto);
                    updatedUser = userService.updateUser(updatedUser);
                    return new ResponseEntity<>(userService.convertToDto(updatedUser), HttpStatus.OK);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.getUserById(id).isPresent()) {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            throw new ResourceNotFoundException("User not found");
        }
    }

    @GetMapping("/search")
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<List<UserDto>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) RoleName role) {
        
        List<UserDto> users = userService.searchUsersByUsername(username, role)
                .stream()
                .map(userService::convertToDto)
                .collect(Collectors.toList());
        
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {
        String newToken = userService.changePassword(authentication.getName(), request);
        return ResponseEntity.ok(newToken);
    }
} 
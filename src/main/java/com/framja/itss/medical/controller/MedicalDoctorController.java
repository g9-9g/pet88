package com.framja.itss.medical.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.framja.itss.common.enums.RoleName;
import com.framja.itss.users.dto.UserSummaryDto;
import com.framja.itss.users.entity.User;
import com.framja.itss.users.repository.UserRepository;

@RestController
@RequestMapping("/api/medical/doctors")
public class MedicalDoctorController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<List<UserSummaryDto>> getAllDoctors() {
        List<User> doctors = userRepository.findByRole(RoleName.ROLE_VET);
        
        List<UserSummaryDto> doctorDtos = doctors.stream()
                .map(doctor -> UserSummaryDto.builder()
                        .id(doctor.getId())
                        .username(doctor.getUsername())
                        .fullName(doctor.getFullName())
                        .email(doctor.getEmail())
                        .build())
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(doctorDtos);
    }
} 
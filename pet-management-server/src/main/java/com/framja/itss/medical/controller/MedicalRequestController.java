package com.framja.itss.medical.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framja.itss.common.enums.MedicalRequestStatus;
import com.framja.itss.medical.dto.request.CreateMedicalRequestDto;
import com.framja.itss.medical.dto.request.MedicalRequestDto;
import com.framja.itss.medical.dto.request.UpdateMedicalRequestDto;
import com.framja.itss.medical.dto.request.UpdateRequestStatusDto;
import com.framja.itss.medical.service.MedicalRequestService;
import com.framja.itss.users.entity.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medical/requests")
public class MedicalRequestController {

    @Autowired
    private MedicalRequestService requestService;


    @PostMapping
    @PreAuthorize("hasRole('ROLE_PET_OWNER')")
    public ResponseEntity<MedicalRequestDto> createRequest(
            @Valid @RequestBody CreateMedicalRequestDto createDto,
            @AuthenticationPrincipal User user) {
        MedicalRequestDto requestDto = requestService.createRequest(createDto, user.getId());
        return ResponseEntity.ok(requestDto);
    }

    @GetMapping
    public ResponseEntity<List<MedicalRequestDto>> getAllRequests(
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) MedicalRequestStatus medicalRequestStatus) {
        List<MedicalRequestDto> requests = requestService.getAllRequests(ownerId, medicalRequestStatus);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countRequests(
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) MedicalRequestStatus medicalRequestStatus) {
        long count = requestService.countRequests(ownerId, medicalRequestStatus);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<MedicalRequestDto> getRequestById(@PathVariable Long requestId) {
        MedicalRequestDto requestDto = requestService.getRequestById(requestId);
        return ResponseEntity.ok(requestDto);
    }

    @GetMapping("/owned")
    @PreAuthorize("hasRole('ROLE_PET_OWNER')")
    public ResponseEntity<List<MedicalRequestDto>> getOwnerRequests(@AuthenticationPrincipal User user) {
        List<MedicalRequestDto> requests = requestService.getRequestsByOwnerId(user.getId());
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<List<MedicalRequestDto>> getPendingRequests() {
        List<MedicalRequestDto> requests = requestService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }

    @PostMapping("/{requestId}/handle")
    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<MedicalRequestDto> handleRequest(
            @PathVariable Long requestId,
            @Valid @RequestBody UpdateRequestStatusDto updateDto,
            @AuthenticationPrincipal User user) {
        updateDto.setStaffId(user.getId());
        if (updateDto.getStatus() == MedicalRequestStatus.ACCEPTED) {
            if (updateDto.getDoctorId() == null) {
                throw new IllegalArgumentException("Doctor ID is required for accepting request");
            }
        } else if (updateDto.getStatus() == MedicalRequestStatus.REJECTED) {
            if (updateDto.getRejectionReason() == null) {
                throw new IllegalArgumentException("Rejection reason is required for rejecting request");
            }
        }
        MedicalRequestDto requestDto = requestService.updateRequestStatus(requestId, updateDto);

        return ResponseEntity.ok(requestDto);
    }
    
    @DeleteMapping("/{requestId}")
    @PreAuthorize("hasRole('ROLE_PET_OWNER', 'ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRequest(
            @PathVariable Long requestId,
            @AuthenticationPrincipal User user) {
        requestService.deleteRequest(requestId, user.getId());
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{requestId}")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<MedicalRequestDto> updateRequest(
            @PathVariable Long requestId,
            @Valid @RequestBody UpdateMedicalRequestDto updateDto,
            @AuthenticationPrincipal User user) {
        MedicalRequestDto requestDto = requestService.updateRequest(requestId, updateDto, user.getId());
        return ResponseEntity.ok(requestDto);
    }
}
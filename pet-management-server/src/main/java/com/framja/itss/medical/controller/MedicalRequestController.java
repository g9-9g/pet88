package com.framja.itss.medical.controller;

import java.util.List;

import com.framja.itss.common.enums.RequestStatus;
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

import com.framja.itss.common.enums.RequestHandleStatus;
import com.framja.itss.medical.dto.HandleRequestDto;
import com.framja.itss.medical.dto.CreateMedicalRequestDto;
import com.framja.itss.medical.dto.MedicalRequestDto;
import com.framja.itss.medical.dto.UpdateMedicalRequestDto;
import com.framja.itss.medical.dto.UpdateRequestStatusDto;
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
            @RequestParam(required = false) String ownerName,
            @RequestParam(required = false) RequestStatus requestStatus) {
        List<MedicalRequestDto> requests = requestService.getAllRequests(ownerName, requestStatus);
        return ResponseEntity.ok(requests);
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
            @Valid @RequestBody HandleRequestDto handleDto,
            @AuthenticationPrincipal User user) {
        UpdateRequestStatusDto updateDto = new UpdateRequestStatusDto();
        if (handleDto.getStatus() == RequestHandleStatus.ACCEPT) {
            updateDto.setRequestStatus(RequestStatus.ACCEPTED);
            if (handleDto.getDoctorId() == null) {
                throw new IllegalArgumentException("Doctor ID is required");
            }
            updateDto.setDoctorId(handleDto.getDoctorId());
        } else {
            updateDto.setRequestStatus(RequestStatus.REJECTED);
            if (handleDto.getRejectionReason() == null) {
                throw new IllegalArgumentException("Rejection reason is required");
            }
            updateDto.setRejectionReason(handleDto.getRejectionReason());
        }
        updateDto.setStaffId(user.getId());
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
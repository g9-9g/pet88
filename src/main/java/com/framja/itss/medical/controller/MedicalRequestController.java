package com.framja.itss.medical.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.framja.itss.common.enums.Status;
import com.framja.itss.medical.dto.CreateMedicalRequestDto;
import com.framja.itss.medical.dto.MedicalRequestDto;
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
    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<List<MedicalRequestDto>> getAllRequests(
            @RequestParam(required = false) String ownerName,
            @RequestParam(required = false) Status status) {
        List<MedicalRequestDto> requests = requestService.getAllRequests(ownerName, status);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{requestId}")
    @PreAuthorize("hasAnyRole('ROLE_PET_OWNER', 'ROLE_STAFF', 'ROLE_VET', 'ROLE_ADMIN')")
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

    @PostMapping("/{requestId}/accept")
    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<MedicalRequestDto> acceptRequest(
            @PathVariable Long requestId, 
            @Valid @RequestBody UpdateRequestStatusDto updateDto) {
        updateDto.setStatus(Status.ACCEPTED);
        MedicalRequestDto requestDto = requestService.updateRequestStatus(requestId, updateDto);
        return ResponseEntity.ok(requestDto);
    }
    
    @PostMapping("/{requestId}/reject")
    @PreAuthorize("hasAnyRole('ROLE_STAFF', 'ROLE_ADMIN')")
    public ResponseEntity<MedicalRequestDto> rejectRequest(
            @PathVariable Long requestId, 
            @Valid @RequestBody UpdateRequestStatusDto updateDto) {
        updateDto.setStatus(Status.REJECTED);
        MedicalRequestDto requestDto = requestService.updateRequestStatus(requestId, updateDto);
        return ResponseEntity.ok(requestDto);
    }
}
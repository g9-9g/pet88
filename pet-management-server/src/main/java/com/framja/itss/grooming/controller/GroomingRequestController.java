package com.framja.itss.grooming.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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

import com.framja.itss.grooming.dto.GroomingRequestDto;
import com.framja.itss.common.enums.GroomingRequestStatus;
import com.framja.itss.grooming.service.GroomingRequestService;
import com.framja.itss.users.entity.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/grooming/requests")
@RequiredArgsConstructor
public class GroomingRequestController {

    private final GroomingRequestService groomingRequestService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_PET_OWNER')")
    public ResponseEntity<GroomingRequestDto> createGroomingRequest(
            @RequestBody GroomingRequestDto.CreateRequest createRequest,
            @AuthenticationPrincipal User user) {
        GroomingRequestDto created = groomingRequestService.createRequest(createRequest, user.getId());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PET_OWNER', 'ROLE_STAFF')")
    public ResponseEntity<GroomingRequestDto> getGroomingRequestById(@PathVariable Long id) {
        return groomingRequestService.getRequestById(id)
                .map(requestDto -> new ResponseEntity<>(requestDto, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<List<GroomingRequestDto>> getAllGroomingRequests() {
        List<GroomingRequestDto> requests = groomingRequestService.getAllRequests();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('ROLE_PET_OWNER')")
    public ResponseEntity<List<GroomingRequestDto>> getMyGroomingRequests(@AuthenticationPrincipal User user) {
        List<GroomingRequestDto> requests = groomingRequestService.getRequestsByOwnerId(user.getId());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/pet/{petId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_PET_OWNER', 'ROLE_STAFF')")
    public ResponseEntity<List<GroomingRequestDto>> getGroomingRequestsByPetId(@PathVariable Long petId) {
        List<GroomingRequestDto> requests = groomingRequestService.getRequestsByPetId(petId);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<List<GroomingRequestDto>> getGroomingRequestsByStatus(
            @PathVariable GroomingRequestStatus status) {
        List<GroomingRequestDto> requests = groomingRequestService.getRequestsByStatus(status);
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @GetMapping("/staff-assigned")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<List<GroomingRequestDto>> getGroomingRequestsAssignedToStaff(
            @AuthenticationPrincipal User user) {
        List<GroomingRequestDto> requests = groomingRequestService.getRequestsByStaffId(user.getId());
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<GroomingRequestDto> updateGroomingRequestStatus(
            @PathVariable Long id,
            @RequestBody GroomingRequestDto.UpdateRequest updateRequest,
            @AuthenticationPrincipal User user) {
        GroomingRequestDto updated = groomingRequestService.updateRequestStatus(id, updateRequest, user.getId());
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteGroomingRequest(@PathVariable Long id) {
        groomingRequestService.deleteRequest(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_STAFF')")
    public ResponseEntity<?> searchGroomingRequests(
            @RequestParam(required = false) Long ownerId,
            @RequestParam(required = false) Long petId,
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) Long staffId,
            @RequestParam(required = false) GroomingRequestStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false, defaultValue = "requestedDateTime") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortDir,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        
        Page<GroomingRequestDto> requestPage = groomingRequestService.getFilteredRequestsWithPagination(
                ownerId, petId, serviceId, staffId, status, startDate, endDate, sortBy, sortDir, page, size);
        
        Map<String, Object> response = new HashMap<>();
        response.put("requests", requestPage.getContent());
        response.put("currentPage", requestPage.getNumber());
        response.put("totalItems", requestPage.getTotalElements());
        response.put("totalPages", requestPage.getTotalPages());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
} 
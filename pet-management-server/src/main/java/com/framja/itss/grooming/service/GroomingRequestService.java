package com.framja.itss.grooming.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import com.framja.itss.grooming.dto.GroomingRequestDto;
import com.framja.itss.common.enums.GroomingRequestStatus;

public interface GroomingRequestService {
    
    GroomingRequestDto createRequest(GroomingRequestDto.CreateRequest createRequest, Long ownerId);
    
    Optional<GroomingRequestDto> getRequestById(Long id);
    
    List<GroomingRequestDto> getAllRequests();
    
    List<GroomingRequestDto> getRequestsByOwnerId(Long ownerId);
    
    List<GroomingRequestDto> getRequestsByPetId(Long petId);
    
    List<GroomingRequestDto> getRequestsByStaffId(Long staffId);
    
    List<GroomingRequestDto> getRequestsByStatus(GroomingRequestStatus status);
    
    GroomingRequestDto updateRequestStatus(Long id, GroomingRequestDto.UpdateRequest updateRequest, Long staffId);
    
    void deleteRequest(Long id);
    
    Page<GroomingRequestDto> getFilteredRequestsWithPagination(
            Long ownerId,
            Long petId,
            Long serviceId,
            Long staffId,
            GroomingRequestStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String sortBy,
            String sortDir,
            int page,
            int size);
} 
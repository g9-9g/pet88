package com.framja.itss.medical.service;

import java.util.List;

import com.framja.itss.common.enums.Status;
import com.framja.itss.medical.dto.CreateMedicalRequestDto;
import com.framja.itss.medical.dto.MedicalRequestDto;
import com.framja.itss.medical.dto.UpdateMedicalRequestDto;
import com.framja.itss.medical.dto.UpdateRequestStatusDto;

public interface MedicalRequestService {
    MedicalRequestDto createRequest(CreateMedicalRequestDto createDto, Long ownerId);
    MedicalRequestDto getRequestById(Long requestId);
    List<MedicalRequestDto> getRequestsByOwnerId(Long ownerId);
    List<MedicalRequestDto> getPendingRequests();
    MedicalRequestDto updateRequestStatus(Long requestId, UpdateRequestStatusDto updateDto);
    List<MedicalRequestDto> getAllRequests(String ownerName, Status status);
    void deleteRequest(Long requestId, Long userId);
    MedicalRequestDto updateRequest(Long requestId, UpdateMedicalRequestDto updateDto, Long userId);
} 
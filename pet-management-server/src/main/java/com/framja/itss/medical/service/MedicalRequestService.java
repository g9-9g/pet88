package com.framja.itss.medical.service;

import java.util.List;

import com.framja.itss.common.enums.MedicalRequestStatus;
import com.framja.itss.common.dto.CountDTO;
import com.framja.itss.medical.dto.request.CreateMedicalRequestDto;
import com.framja.itss.medical.dto.request.MedicalRequestDto;
import com.framja.itss.medical.dto.request.UpdateMedicalRequestDto;
import com.framja.itss.medical.dto.request.UpdateRequestStatusDto;

public interface MedicalRequestService {
    MedicalRequestDto createRequest(CreateMedicalRequestDto createDto, Long ownerId);
    MedicalRequestDto getRequestById(Long requestId);
    List<MedicalRequestDto> getRequestsByOwnerId(Long ownerId);
    List<MedicalRequestDto> getPendingRequests();
    MedicalRequestDto updateRequestStatus(Long requestId, UpdateRequestStatusDto updateDto);
    List<MedicalRequestDto> getAllRequests(Long ownerId, MedicalRequestStatus medicalRequestStatus);
    void deleteRequest(Long requestId, Long userId);
    MedicalRequestDto updateRequest(Long requestId, UpdateMedicalRequestDto updateDto, Long userId);
    long countRequests(Long ownerId, MedicalRequestStatus medicalRequestStatus);
    CountDTO getRequestCountsAll();
    CountDTO getRequestCountsByOwnerId(Long ownerId);
} 
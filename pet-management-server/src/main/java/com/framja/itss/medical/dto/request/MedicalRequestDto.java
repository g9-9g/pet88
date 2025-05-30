package com.framja.itss.medical.dto.request;

import java.time.LocalDateTime;

import com.framja.itss.common.enums.MedicalRequestStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRequestDto {
    private Long id;
    private Long petId;
    private String petName;
    private Long ownerId;
    private String ownerName;
    private Long updatedById;
    private String updatedByName;
    private String symptoms;
    private String notes;
    private LocalDateTime preferredDateTime;
    private MedicalRequestStatus status;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 
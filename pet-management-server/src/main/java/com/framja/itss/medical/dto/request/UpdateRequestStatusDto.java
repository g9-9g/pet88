package com.framja.itss.medical.dto.request;

import com.framja.itss.common.enums.MedicalRequestStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequestStatusDto {
    @NotNull(message = "MedicalRequestStatus is required")
    private MedicalRequestStatus status;
    
    private Long doctorId;
    private String rejectionReason;
    private Long staffId;
} 
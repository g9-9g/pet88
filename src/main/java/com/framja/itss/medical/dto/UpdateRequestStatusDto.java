package com.framja.itss.medical.dto;

import com.framja.itss.common.enums.Status;

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
    @NotNull(message = "Status is required")
    private Status status;
    
    private Long doctorId;
    private String rejectionReason;
    private Long staffId;
} 
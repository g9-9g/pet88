package com.framja.itss.medical.dto;

import com.framja.itss.common.enums.RequestHandleStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandleRequestDto {
    @NotNull(message = "RequestStatus is required")
    private RequestHandleStatus status;
    
    private Long doctorId;
    private String rejectionReason;
} 
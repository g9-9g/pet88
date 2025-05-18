package com.framja.itss.medical.dto;

import com.framja.itss.common.enums.AppointmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppointmentDto {
    private String diagnosis;
    private String treatment;
    private String notes;
    private AppointmentStatus status;
} 
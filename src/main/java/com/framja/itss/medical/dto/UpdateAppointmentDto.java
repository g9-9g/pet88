package com.framja.itss.medical.dto;

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
    private Boolean completed;
    private Boolean cancelled;
} 
package com.framja.itss.medical.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentDto {
    private Long petId;
    private LocalDateTime appointmentDateTime;
    private String diagnosis;
    private String treatment;
    private String notes;
} 
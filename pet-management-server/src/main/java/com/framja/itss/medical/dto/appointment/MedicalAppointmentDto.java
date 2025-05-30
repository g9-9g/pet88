package com.framja.itss.medical.dto.appointment;

import java.time.LocalDateTime;

import com.framja.itss.common.enums.AppointmentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalAppointmentDto {
    private Long id;
    private Long requestId;
    private Long petId;
    private String petName;
    private Long ownerId;
    private String ownerName;
    private Long doctorId;
    private String doctorName;
    private LocalDateTime appointmentDateTime;
    private String symptoms;
    private String diagnosis;
    private String treatment;
    private String notes;
    private AppointmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 
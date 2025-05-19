package com.framja.itss.medical.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.framja.itss.common.enums.AppointmentStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MedicalAppointmentDetailDto extends MedicalAppointmentDto {
    private List<PrescriptionDto> prescriptions;
} 
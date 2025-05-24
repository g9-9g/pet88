package com.framja.itss.medical.dto.appointment;

import java.util.List;

import com.framja.itss.medical.dto.medicine.PrescriptionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class MedicalAppointmentDetailDto extends MedicalAppointmentDto {
    private List<PrescriptionDto> prescriptions;
} 
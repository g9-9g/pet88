package com.framja.itss.medical.dto.medicine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDto {
    private Long id;
    private Long appointmentId;
    private Long medicineId;
    private String medicineName;
    private Integer quantity;
    private String usageInstructions;
} 
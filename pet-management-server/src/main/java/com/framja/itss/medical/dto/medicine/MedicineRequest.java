package com.framja.itss.medical.dto.medicine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRequest {
    private String name;
    private String description;
    private String unit;
    private Double unitPrice;
} 
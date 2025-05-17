package com.framja.itss.medical.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMedicalRequestDto {
    @NotNull
    private Long petId;
    
    private String symptoms;
    
    private String notes;
    
    private LocalDateTime preferredDateTime;
} 
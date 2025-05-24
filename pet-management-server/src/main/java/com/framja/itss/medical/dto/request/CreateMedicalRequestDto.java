package com.framja.itss.medical.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMedicalRequestDto {
    @NotNull(message = "Pet ID is required")
    private Long petId;
    
    private String symptoms;
    private String notes;
    
    @NotNull(message = "Preferred date and time is required")
    @Future(message = "Preferred date and time must be in the future")
    private LocalDateTime preferredDateTime;
} 
package com.framja.itss.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {
    private Long petId;
    private Long ownerId;
    private String name;
    private String species;
    private String breed;
    private String gender;
    private LocalDate birthdate;
    private String color;
    private String avatarUrl;
    private String healthNotes;
    private String vaccinationHistory;
    private String nutritionNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 
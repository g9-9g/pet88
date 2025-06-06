package com.framja.itss.grooming.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.framja.itss.common.enums.GroomingRequestStatus;
import com.framja.itss.pets.dto.PetDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroomingRequestDto {
    private Long id;
    private Long serviceId;
    private Long petId;
    private Long ownerId;
    private Long staffId;
    private LocalDateTime requestedDateTime;
    private LocalDateTime scheduledDateTime;
    private GroomingRequestStatus status;
    private String notes;
    private String staffNotes;
    private LocalDateTime completedDateTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private GroomingServiceDto service;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PetDto pet;
    
    // Simplified version with just IDs for create/update operations
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        private Long serviceId;
        private Long petId;
        private LocalDateTime requestedDateTime;
        private String notes;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private GroomingRequestStatus status;
        private LocalDateTime scheduledDateTime;
        private String staffNotes;
    }
} 
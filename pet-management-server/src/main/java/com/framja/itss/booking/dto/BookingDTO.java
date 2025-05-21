package com.framja.itss.booking.dto;

import com.framja.itss.booking.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private Long petId;
    private Long ownerId;
    private Long roomId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private BookingStatus status;
    private boolean isConfirmed;
    private String specialCareNotes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class CreateBookingRequest {
    private Long petId;
    private Long roomId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String specialCareNotes;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UpdateBookingRequest {
    private Long petId;
    private Long roomId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private String specialCareNotes;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class UpdateBookingStatusRequest {
    private BookingStatus status;
} 
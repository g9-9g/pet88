package com.framja.itss.booking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.framja.itss.booking.entity.BookingStatus;
import com.framja.itss.booking.entity.RoomType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDTO {
    private Long id;
    private Long petId;
    private String petName;
    private Long ownerId;
    private Long roomId;
    private RoomType roomType;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private BookingStatus status;
    private boolean isConfirmed;
    private String specialCareNotes;
    private BigDecimal estimatedFee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package com.framja.itss.booking.dto;

import com.framja.itss.booking.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
    private Long id;
    private String description;
    private RoomType type;
    private BigDecimal nightlyFee;
    private BigDecimal cleanFee;
    private BigDecimal serviceFee;
    private BigDecimal averageFee;
    private boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

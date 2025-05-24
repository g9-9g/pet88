package com.framja.itss.booking.dto;

import com.framja.itss.booking.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    private String description;
    private RoomType type;
    private BigDecimal nightlyFee;
    private BigDecimal cleanFee;
    private BigDecimal serviceFee;
    
    @Builder.Default
    private boolean isAvailable = false;
} 
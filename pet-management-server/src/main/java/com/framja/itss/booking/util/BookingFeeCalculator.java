package com.framja.itss.booking.util;

import com.framja.itss.booking.entity.RoomType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class BookingFeeCalculator {

    // Multiplier logic based on RoomType
    private static BigDecimal getRoomTypeMultiplier(RoomType type) {
        return switch (type) {
            case DELUXE -> new BigDecimal("1.10");
            case LUXURY -> new BigDecimal("1.20");
            case SUITE  -> new BigDecimal("1.30");
            default     -> BigDecimal.ONE;
        };
    }

    // Function 1: Calculate total fee
    public static BigDecimal calculateTotalFee(
            BigDecimal nightlyFee,
            BigDecimal cleanFee,
            BigDecimal serviceFee,
            LocalDateTime checkInTime,
            LocalDateTime checkOutTime,
            RoomType roomType
    ) {
        if (checkInTime == null || checkOutTime == null || !checkOutTime.isAfter(checkInTime)) {
            throw new IllegalArgumentException("Invalid check-in/check-out time.");
        }

        long nights = ChronoUnit.DAYS.between(checkInTime.toLocalDate(), checkOutTime.toLocalDate());
        if (checkOutTime.toLocalTime().isAfter(checkInTime.toLocalTime())) {
            nights += 1;
        }

        BigDecimal multiplier = getRoomTypeMultiplier(roomType);
        BigDecimal adjustedNightlyFee = nightlyFee.multiply(multiplier);

        BigDecimal total = adjustedNightlyFee.multiply(BigDecimal.valueOf(nights))
                .add(cleanFee)
                .add(serviceFee);

        return total.setScale(2, RoundingMode.HALF_UP);
    }

    // Function 2: Calculate average fee per day with fixed estimatedNights = 10
    public static BigDecimal calculateAverageFeePerDay(
            BigDecimal nightlyFee,
            BigDecimal cleanFee,
            BigDecimal serviceFee,
            RoomType roomType
    ) {
        int estimatedNights = 10;

        BigDecimal multiplier = getRoomTypeMultiplier(roomType);
        BigDecimal adjustedNightlyFee = nightlyFee.multiply(multiplier);

        BigDecimal total = adjustedNightlyFee.multiply(BigDecimal.valueOf(estimatedNights))
                .add(cleanFee)
                .add(serviceFee);

        return total
                .divide(BigDecimal.valueOf(estimatedNights), 2, RoundingMode.HALF_UP);
    }
}

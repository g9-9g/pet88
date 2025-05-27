package com.framja.itss.booking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import com.framja.itss.booking.util.BookingFeeCalculator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time", nullable = false)
    private LocalDateTime checkOutTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    private boolean isConfirmed = false;

    @Column(name = "special_care_notes", length = 1000)
    private String specialCareNotes;

    @Formula("(SELECT calculate_total_fee(r.nightly_fee, r.clean_fee, r.service_fee, b1_0.check_in_time, b1_0.check_out_time, r.type) " +
             "FROM rooms r WHERE r.id = b1_0.room_id)")
    private BigDecimal estimatedFee;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PostLoad
    protected void onLoad() {
        if (room != null && checkInTime != null && checkOutTime != null) {
            estimatedFee = BookingFeeCalculator.calculateTotalFee(
                room.getNightlyFee(),
                room.getCleanFee(),
                room.getServiceFee(),
                checkInTime,
                checkOutTime,
                room.getType()
            );
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
} 
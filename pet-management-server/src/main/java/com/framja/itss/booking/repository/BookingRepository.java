package com.framja.itss.booking.repository;

import com.framja.itss.booking.entity.Booking;
import com.framja.itss.booking.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    // Tìm tất cả booking của một owner
    List<Booking> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);
    
    // Tìm booking theo status
    List<Booking> findByStatusOrderByCreatedAtDesc(BookingStatus status);
    
    // Tìm booking theo owner và status
    List<Booking> findByOwnerIdAndStatusOrderByCreatedAtDesc(Long ownerId, BookingStatus status);
    
    // Kiểm tra xem owner có sở hữu booking này không
    boolean existsByIdAndOwnerId(Long id, Long ownerId);
} 
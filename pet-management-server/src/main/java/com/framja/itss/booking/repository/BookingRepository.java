package com.framja.itss.booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.framja.itss.booking.entity.Booking;
import com.framja.itss.booking.entity.BookingStatus;

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
    
    // Kiểm tra trùng ngày booking
    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.room.id = :roomId " +
           "AND b.status NOT IN ('CANCELLED', 'COMPLETED') " +
           "AND ((b.checkInTime <= :endTime AND b.checkOutTime >= :startTime) OR " +
           "(b.checkInTime >= :startTime AND b.checkInTime < :endTime))")
    boolean existsOverlappingBooking(@Param("roomId") Long roomId,
                                   @Param("startTime") LocalDateTime startTime,
                                   @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.ownerId = :ownerId")
    Long countByOwnerId(@Param("ownerId") Long ownerId);
    
    @Query("SELECT b.status as status, COUNT(b) as count FROM Booking b " +
           "WHERE b.ownerId = :ownerId GROUP BY b.status")
    List<Object[]> countByStatusAndOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT b.status as status, COUNT(b) as count FROM Booking b " +
           "GROUP BY b.status")
    List<Object[]> countByStatusAll();

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE " +
           "b.room.id = :roomId AND " +
           "((b.checkInTime <= :checkOutTime AND b.checkOutTime >= :checkInTime))")
    boolean hasOverlappingBooking(
        @Param("roomId") Long roomId,
        @Param("checkInTime") LocalDateTime checkInTime,
        @Param("checkOutTime") LocalDateTime checkOutTime
    );
} 
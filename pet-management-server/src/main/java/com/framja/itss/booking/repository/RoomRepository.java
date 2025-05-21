package com.framja.itss.booking.repository;

import com.framja.itss.booking.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    
    @Query("SELECT r FROM Room r WHERE r.isAvailable = true AND r.id NOT IN " +
           "(SELECT b.room.id FROM Booking b WHERE " +
           "((b.checkInTime <= :endTime AND b.checkOutTime >= :startTime) OR " +
           "(b.checkInTime >= :startTime AND b.checkInTime < :endTime)) AND " +
           "b.status NOT IN ('CANCELLED', 'COMPLETED'))")
    List<Room> findAvailableRooms(@Param("startTime") LocalDateTime startTime, 
                                 @Param("endTime") LocalDateTime endTime);

    List<Room> findByIsAvailableTrue();
} 
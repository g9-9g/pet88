package com.framja.itss.booking.service;

import com.framja.itss.booking.dto.RoomDTO;
import com.framja.itss.booking.entity.Room;
import com.framja.itss.booking.entity.RoomType;
import com.framja.itss.common.dto.CountDTO;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import com.framja.itss.booking.dto.RoomRequest;

public interface RoomService {
    List<RoomDTO> getAllRooms();
    RoomDTO getRoomById(Long id);
    RoomDTO createRoom(RoomRequest roomRequest);
    RoomDTO updateRoom(Long id, RoomRequest roomRequest);
    void deleteRoom(Long id);
    List<RoomDTO> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime, RoomType type, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, String sortDir);
    RoomDTO updateRoomAvailability(Long id, boolean isAvailable);
    CountDTO getRoomCountsByType();
} 
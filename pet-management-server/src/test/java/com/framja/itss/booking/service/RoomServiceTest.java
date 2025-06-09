package com.framja.itss.booking.service;

import com.framja.itss.booking.dto.RoomDTO;
import com.framja.itss.booking.dto.RoomRequest;
import com.framja.itss.booking.entity.Room;
import com.framja.itss.booking.entity.RoomType;
import com.framja.itss.booking.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoomServiceTest {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomServiceImpl roomService;

    private Room room;
    private RoomDTO roomDTO;
    private RoomRequest roomRequest;

    @BeforeEach
    void setUp() {
        // Setup room
        room = new Room();
        ReflectionTestUtils.setField(room, "id", 1L);
        room.setRoomNumber("101");
        room.setType(RoomType.STANDARD);
        room.setPrice(new BigDecimal("100.00"));
        room.setAvailable(true);

        // Setup roomDTO
        roomDTO = new RoomDTO();
        roomDTO.setId(1L);
        roomDTO.setRoomNumber("101");
        roomDTO.setType(RoomType.STANDARD);
        roomDTO.setPrice(new BigDecimal("100.00"));
        roomDTO.setAvailable(true);

        // Setup roomRequest
        roomRequest = new RoomRequest();
        roomRequest.setRoomNumber("101");
        roomRequest.setType(RoomType.STANDARD);
        roomRequest.setPrice(new BigDecimal("100.00"));
        roomRequest.setAvailable(true);
    }

    @Test
    void getAllRooms_ShouldReturnAllRooms() {
        // Arrange
        when(roomRepository.findAll()).thenReturn(Arrays.asList(room));

        // Act
        List<RoomDTO> result = roomService.getAllRooms();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roomRepository).findAll();
    }

    @Test
    void getRoomById_ShouldReturnRoom() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));

        // Act
        RoomDTO result = roomService.getRoomById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(roomRepository).findById(1L);
    }

    @Test
    void createRoom_ShouldCreateNewRoom() {
        // Arrange
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        RoomDTO result = roomService.createRoom(roomRequest);

        // Assert
        assertNotNull(result);
        assertEquals("101", result.getRoomNumber());
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void updateRoom_ShouldUpdateRoom() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        RoomDTO result = roomService.updateRoom(1L, roomRequest);

        // Assert
        assertNotNull(result);
        assertEquals("101", result.getRoomNumber());
        verify(roomRepository).findById(1L);
        verify(roomRepository).save(any(Room.class));
    }

    @Test
    void deleteRoom_ShouldDeleteRoom() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        doNothing().when(roomRepository).delete(any(Room.class));

        // Act
        roomService.deleteRoom(1L);

        // Assert
        verify(roomRepository).findById(1L);
        verify(roomRepository).delete(any(Room.class));
    }

    @Test
    void getAvailableRooms_ShouldReturnAvailableRooms() {
        // Arrange
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = LocalDateTime.now().plusDays(1);
        BigDecimal minPrice = new BigDecimal("50.00");
        BigDecimal maxPrice = new BigDecimal("200.00");
        String sortBy = "price";
        String sortDir = "asc";

        when(roomRepository.findAvailableRooms(
            eq(startTime),
            eq(endTime),
            eq(RoomType.STANDARD),
            eq(minPrice),
            eq(maxPrice),
            any(Sort.class)
        )).thenReturn(Arrays.asList(room));

        // Act
        List<RoomDTO> result = roomService.getAvailableRooms(
            startTime,
            endTime,
            RoomType.STANDARD,
            minPrice,
            maxPrice,
            sortBy,
            sortDir
        );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roomRepository).findAvailableRooms(
            eq(startTime),
            eq(endTime),
            eq(RoomType.STANDARD),
            eq(minPrice),
            eq(maxPrice),
            any(Sort.class)
        );
    }

    @Test
    void updateRoomAvailability_ShouldUpdateAvailability() {
        // Arrange
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act
        RoomDTO result = roomService.updateRoomAvailability(1L, false);

        // Assert
        assertNotNull(result);
        assertFalse(result.isAvailable());
        verify(roomRepository).findById(1L);
        verify(roomRepository).save(any(Room.class));
    }
} 
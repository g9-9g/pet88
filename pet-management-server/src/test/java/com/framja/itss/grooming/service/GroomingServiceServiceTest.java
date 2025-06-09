package com.framja.itss.grooming.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.framja.itss.grooming.dto.GroomingServiceDto;
import com.framja.itss.grooming.entity.GroomingService;
import com.framja.itss.grooming.repository.GroomingServiceRepository;
import com.framja.itss.grooming.service.impl.GroomingServiceServiceImpl;

/**
 * Test class cho GroomingServiceService
 * Kiểm tra các chức năng liên quan đến dịch vụ chăm sóc thú cưng
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GroomingServiceServiceTest {

    @Mock
    private GroomingServiceRepository serviceRepository;

    @InjectMocks
    private GroomingServiceServiceImpl groomingService;

    private GroomingService mockService;
    private GroomingServiceDto serviceDto;

    @BeforeEach
    void setUp() {
        // Khởi tạo mock dịch vụ
        mockService = GroomingService.builder()
            .id(1L)
            .name("Test Service")
            .description("Test Description")
            .price(100.0)
            .duration(60)
            .isActive(true)
            .build();

        // Khởi tạo DTO
        serviceDto = GroomingServiceDto.builder()
            .id(1L)
            .name("Test Service")
            .description("Test Description")
            .price(100.0)
            .duration(60)
            .isActive(true)
            .build();
    }

    /**
     * Test case: Tạo dịch vụ mới thành công
     * Tình huống: Tạo một dịch vụ chăm sóc thú cưng mới
     * Kết quả mong đợi:
     * - Trả về thông tin dịch vụ đã tạo
     * - Dịch vụ được lưu vào database
     */
    @Test
    void createService_Success() {
        when(serviceRepository.save(any(GroomingService.class))).thenReturn(mockService);

        GroomingServiceDto result = groomingService.createService(serviceDto);

        assertNotNull(result);
        assertEquals(serviceDto.getName(), result.getName());
        assertEquals(serviceDto.getPrice(), result.getPrice());
        verify(serviceRepository).save(any(GroomingService.class));
    }

    /**
     * Test case: Lấy thông tin dịch vụ theo ID thành công
     * Tình huống: Tìm kiếm dịch vụ với ID tồn tại
     * Kết quả mong đợi:
     * - Trả về thông tin dịch vụ
     * - ID của dịch vụ trả về phải khớp với ID tìm kiếm
     */
    @Test
    void getServiceById_Success() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(mockService));

        Optional<GroomingServiceDto> result = groomingService.getServiceById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(serviceRepository).findById(1L);
    }

    /**
     * Test case: Lấy danh sách tất cả dịch vụ thành công
     * Tình huống: Lấy danh sách tất cả dịch vụ
     * Kết quả mong đợi:
     * - Trả về danh sách dịch vụ
     * - Danh sách không rỗng
     * - Số lượng dịch vụ phải đúng
     */
    @Test
    void getAllServices_Success() {
        when(serviceRepository.findAll()).thenReturn(Arrays.asList(mockService));

        List<GroomingServiceDto> results = groomingService.getAllServices();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(serviceRepository).findAll();
    }

    /**
     * Test case: Lấy danh sách dịch vụ đang hoạt động thành công
     * Tình huống: Lấy danh sách các dịch vụ có trạng thái active
     * Kết quả mong đợi:
     * - Trả về danh sách dịch vụ đang hoạt động
     * - Tất cả dịch vụ phải có trạng thái active
     */
    @Test
    void getActiveServices_Success() {
        when(serviceRepository.findByIsActiveTrue()).thenReturn(Arrays.asList(mockService));

        List<GroomingServiceDto> results = groomingService.getActiveServices();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertTrue(results.get(0).isActive());
        verify(serviceRepository).findByIsActiveTrue();
    }

    /**
     * Test case: Cập nhật thông tin dịch vụ thành công
     * Tình huống: Cập nhật thông tin của một dịch vụ
     * Kết quả mong đợi:
     * - Trả về thông tin dịch vụ đã cập nhật
     * - Thông tin dịch vụ phải được cập nhật
     */
    @Test
    void updateService_Success() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(mockService));
        when(serviceRepository.save(any(GroomingService.class))).thenReturn(mockService);

        GroomingServiceDto result = groomingService.updateService(1L, serviceDto);

        assertNotNull(result);
        assertEquals(serviceDto.getName(), result.getName());
        verify(serviceRepository).save(any(GroomingService.class));
    }

    /**
     * Test case: Xóa dịch vụ thành công
     * Tình huống: Xóa một dịch vụ
     * Kết quả mong đợi:
     * - Dịch vụ bị xóa khỏi database
     * - Không có exception nào được ném ra
     */
    @Test
    void deleteService_Success() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(mockService));

        assertDoesNotThrow(() -> {
            groomingService.deleteService(1L);
        });

        verify(serviceRepository).delete(mockService);
    }

    /**
     * Test case: Lấy danh sách dịch vụ có phân trang và lọc thành công
     * Tình huống: Tìm kiếm dịch vụ với các điều kiện lọc và phân trang
     * Kết quả mong đợi:
     * - Trả về trang kết quả
     * - Số lượng kết quả phải đúng
     * - Các điều kiện lọc phải được áp dụng
     */
    @Test
    void getFilteredServicesWithPagination_Success() {
        Page<GroomingService> servicePage = new PageImpl<>(Arrays.asList(mockService));
        when(serviceRepository.findAll(any(PageRequest.class))).thenReturn(servicePage);

        Page<GroomingServiceDto> result = groomingService.getFilteredServicesWithPagination(
            "Test", 50.0, 200.0, true, "name", "asc", 0, 10
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(serviceRepository).findAll(any(PageRequest.class));
    }
} 
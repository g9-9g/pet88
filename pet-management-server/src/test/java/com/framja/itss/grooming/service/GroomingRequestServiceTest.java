package com.framja.itss.grooming.service;

import java.time.LocalDateTime;
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

import com.framja.itss.common.enums.GroomingRequestStatus;
import com.framja.itss.common.enums.RoleName;
import com.framja.itss.grooming.dto.GroomingRequestDto;
import com.framja.itss.grooming.entity.GroomingRequest;
import com.framja.itss.grooming.repository.GroomingRequestRepository;
import com.framja.itss.grooming.service.impl.GroomingRequestServiceImpl;
import com.framja.itss.pets.entity.Pet;
import com.framja.itss.users.entity.User;

/**
 * Test class cho GroomingRequestService
 * Kiểm tra các chức năng liên quan đến yêu cầu chăm sóc thú cưng
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GroomingRequestServiceTest {

    @Mock
    private GroomingRequestRepository requestRepository;

    @InjectMocks
    private GroomingRequestServiceImpl requestService;

    private GroomingRequest mockRequest;
    private GroomingRequestDto.CreateRequest createDto;
    private GroomingRequestDto.UpdateRequest updateDto;
    private User mockOwner;
    private User mockStaff;
    private Pet mockPet;

    @BeforeEach
    void setUp() {
        // Khởi tạo mock chủ thú cưng
        mockOwner = User.builder()
            .id(1L)
            .username("owner")
            .password("password")
            .role(RoleName.ROLE_PET_OWNER)
            .build();

        // Khởi tạo mock nhân viên
        mockStaff = User.builder()
            .id(2L)
            .username("staff")
            .password("password")
            .role(RoleName.ROLE_STAFF)
            .build();

        // Khởi tạo mock thú cưng
        mockPet = Pet.builder()
            .petId(1L)
            .name("Test Pet")
            .species("Dog")
            .owner(mockOwner)
            .build();

        // Khởi tạo mock yêu cầu
        mockRequest = GroomingRequest.builder()
            .id(1L)
            .owner(mockOwner)
            .pet(mockPet)
            .status(GroomingRequestStatus.PENDING)
            .requestedDateTime(LocalDateTime.now())
            .notes("Test notes")
            .build();

        // Khởi tạo DTO tạo yêu cầu mới
        createDto = GroomingRequestDto.CreateRequest.builder()
            .petId(1L)
            .requestedDateTime(LocalDateTime.now())
            .notes("Test notes")
            .build();

        // Khởi tạo DTO cập nhật yêu cầu
        updateDto = GroomingRequestDto.UpdateRequest.builder()
            .status(GroomingRequestStatus.ACCEPTED)
            .notes("Updated notes")
            .build();
    }

    /**
     * Test case: Tạo yêu cầu mới thành công
     * Tình huống: Chủ thú cưng tạo yêu cầu chăm sóc mới
     * Kết quả mong đợi:
     * - Trả về thông tin yêu cầu đã tạo
     * - Yêu cầu được lưu vào database
     */
    @Test
    void createRequest_Success() {
        when(requestRepository.save(any(GroomingRequest.class))).thenReturn(mockRequest);

        GroomingRequestDto result = requestService.createRequest(createDto, 1L);

        assertNotNull(result);
        assertEquals(GroomingRequestStatus.PENDING, result.getStatus());
        verify(requestRepository).save(any(GroomingRequest.class));
    }

    /**
     * Test case: Lấy thông tin yêu cầu theo ID thành công
     * Tình huống: Tìm kiếm yêu cầu với ID tồn tại
     * Kết quả mong đợi:
     * - Trả về thông tin yêu cầu
     * - ID của yêu cầu trả về phải khớp với ID tìm kiếm
     */
    @Test
    void getRequestById_Success() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(mockRequest));

        Optional<GroomingRequestDto> result = requestService.getRequestById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(requestRepository).findById(1L);
    }

    /**
     * Test case: Lấy danh sách tất cả yêu cầu thành công
     * Tình huống: Lấy danh sách tất cả yêu cầu
     * Kết quả mong đợi:
     * - Trả về danh sách yêu cầu
     * - Danh sách không rỗng
     * - Số lượng yêu cầu phải đúng
     */
    @Test
    void getAllRequests_Success() {
        when(requestRepository.findAll()).thenReturn(Arrays.asList(mockRequest));

        List<GroomingRequestDto> results = requestService.getAllRequests();

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        verify(requestRepository).findAll();
    }

    /**
     * Test case: Lấy danh sách yêu cầu của chủ thú cưng thành công
     * Tình huống: Tìm kiếm yêu cầu của chủ thú cưng
     * Kết quả mong đợi:
     * - Trả về danh sách yêu cầu
     * - Tất cả yêu cầu phải thuộc về chủ thú cưng đó
     */
    @Test
    void getRequestsByOwnerId_Success() {
        when(requestRepository.findByOwnerId(1L)).thenReturn(Arrays.asList(mockRequest));

        List<GroomingRequestDto> results = requestService.getRequestsByOwnerId(1L);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1L, results.get(0).getOwnerId());
        verify(requestRepository).findByOwnerId(1L);
    }

    /**
     * Test case: Lấy danh sách yêu cầu theo trạng thái thành công
     * Tình huống: Tìm kiếm yêu cầu theo trạng thái
     * Kết quả mong đợi:
     * - Trả về danh sách yêu cầu
     * - Tất cả yêu cầu phải có trạng thái tương ứng
     */
    @Test
    void getRequestsByStatus_Success() {
        when(requestRepository.findByStatus(GroomingRequestStatus.PENDING))
            .thenReturn(Arrays.asList(mockRequest));

        List<GroomingRequestDto> results = requestService.getRequestsByStatus(GroomingRequestStatus.PENDING);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(GroomingRequestStatus.PENDING, results.get(0).getStatus());
        verify(requestRepository).findByStatus(GroomingRequestStatus.PENDING);
    }

    /**
     * Test case: Cập nhật trạng thái yêu cầu thành công
     * Tình huống: Nhân viên cập nhật trạng thái yêu cầu
     * Kết quả mong đợi:
     * - Trả về thông tin yêu cầu đã cập nhật
     * - Trạng thái yêu cầu phải được cập nhật
     */
    @Test
    void updateRequestStatus_Success() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(mockRequest));
        when(requestRepository.save(any(GroomingRequest.class))).thenReturn(mockRequest);

        GroomingRequestDto result = requestService.updateRequestStatus(1L, updateDto, 2L);

        assertNotNull(result);
        assertEquals(GroomingRequestStatus.ACCEPTED, result.getStatus());
        verify(requestRepository).save(any(GroomingRequest.class));
    }

    /**
     * Test case: Xóa yêu cầu thành công
     * Tình huống: Xóa một yêu cầu
     * Kết quả mong đợi:
     * - Yêu cầu bị xóa khỏi database
     * - Không có exception nào được ném ra
     */
    @Test
    void deleteRequest_Success() {
        when(requestRepository.findById(1L)).thenReturn(Optional.of(mockRequest));

        assertDoesNotThrow(() -> {
            requestService.deleteRequest(1L);
        });

        verify(requestRepository).delete(mockRequest);
    }

    /**
     * Test case: Lấy danh sách yêu cầu có phân trang và lọc thành công
     * Tình huống: Tìm kiếm yêu cầu với các điều kiện lọc và phân trang
     * Kết quả mong đợi:
     * - Trả về trang kết quả
     * - Số lượng kết quả phải đúng
     * - Các điều kiện lọc phải được áp dụng
     */
    @Test
    void getFilteredRequestsWithPagination_Success() {
        Page<GroomingRequest> requestPage = new PageImpl<>(Arrays.asList(mockRequest));
        when(requestRepository.findAll(any(PageRequest.class))).thenReturn(requestPage);

        Page<GroomingRequestDto> result = requestService.getFilteredRequestsWithPagination(
            1L, 1L, 1L, 2L, GroomingRequestStatus.PENDING,
            LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1),
            "requestedDateTime", "desc", 0, 10
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(requestRepository).findAll(any(PageRequest.class));
    }
} 
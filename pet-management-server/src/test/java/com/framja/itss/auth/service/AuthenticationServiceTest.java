package com.framja.itss.auth.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.framja.itss.auth.dto.AuthRequest;
import com.framja.itss.auth.dto.AuthResponse;
import com.framja.itss.auth.dto.RegisterRequest;
import com.framja.itss.auth.security.JwtTokenProvider;
import com.framja.itss.auth.service.impl.AuthenticationServiceImpl;
import com.framja.itss.common.enums.RoleName;
import com.framja.itss.common.service.UserQueryService;
import com.framja.itss.users.entity.User;

/**
 * Test class cho AuthenticationService
 * Kiểm tra các chức năng đăng ký và xác thực người dùng
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserQueryService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private RegisterRequest registerRequest;
    private AuthRequest authRequest;
    private User mockUser;
    private static final String TEST_TOKEN = "";

    @BeforeEach
    void setUp() {
        // Khởi tạo dữ liệu test cho đăng ký
        registerRequest = RegisterRequest.builder()
            .username("testuser")
            .password("password123")
            .email("test@example.com")
            .fullName("Test User")
            .role(RoleName.ROLE_PET_OWNER)
            .build();

        // Khởi tạo dữ liệu test cho đăng nhập
        authRequest = AuthRequest.builder()
            .username("testuser")
            .password("password123")
            .build();

        // Khởi tạo mock user
        mockUser = User.builder()
            .id(1L)
            .username("testuser")
            .password("encodedPassword")
            .email("test@example.com")
            .fullName("Test User")
            .role(RoleName.ROLE_PET_OWNER)
            .build();

        // Cấu hình mock cho JWT token provider
        lenient().when(jwtTokenProvider.generateToken(any(User.class))).thenReturn(TEST_TOKEN);
    }

    /**
     * Test case: Đăng ký thành công
     * Tình huống: Người dùng đăng ký với thông tin hợp lệ
     * Kết quả mong đợi: 
     * - Trả về token JWT
     * - Trả về thông tin người dùng đã đăng ký
     * - Lưu người dùng vào database
     */
    @Test
    void register_Success() {
        // Given
        when(userService.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(userService.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(userService.saveUser(
            registerRequest.getUsername(),
            registerRequest.getPassword(),
            registerRequest.getEmail(),
            registerRequest.getFullName(),
            registerRequest.getRole()
        )).thenReturn(mockUser);

        // When
        AuthResponse response = authenticationService.register(registerRequest);

        // Then
        assertNotNull(response);
        assertEquals(TEST_TOKEN, response.getToken());
        assertEquals(mockUser.getId(), response.getUserId());
        assertEquals(mockUser.getUsername(), response.getUsername());
        assertEquals(mockUser.getRole(), response.getRole());
        verify(userService).saveUser(
            registerRequest.getUsername(),
            registerRequest.getPassword(),
            registerRequest.getEmail(),
            registerRequest.getFullName(),
            registerRequest.getRole()
        );
    }

    /**
     * Test case: Đăng ký thất bại - Username đã tồn tại
     * Tình huống: Người dùng đăng ký với username đã được sử dụng
     * Kết quả mong đợi: 
     * - Ném ra RuntimeException
     * - Không lưu người dùng vào database
     */
    @Test
    void register_UsernameExists() {
        // Given
        when(userService.existsByUsername(registerRequest.getUsername())).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            authenticationService.register(registerRequest);
        });
        verify(userService, never()).saveUser(any(), any(), any(), any(), any());
    }

    /**
     * Test case: Đăng ký thất bại - Email đã tồn tại
     * Tình huống: Người dùng đăng ký với email đã được sử dụng
     * Kết quả mong đợi: 
     * - Ném ra RuntimeException
     * - Không lưu người dùng vào database
     */
    @Test
    void register_EmailExists() {
        // Given
        when(userService.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        when(userService.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            authenticationService.register(registerRequest);
        });
        verify(userService, never()).saveUser(any(), any(), any(), any(), any());
    }

    /**
     * Test case: Đăng nhập thành công
     * Tình huống: Người dùng đăng nhập với thông tin đúng
     * Kết quả mong đợi: 
     * - Trả về token JWT
     * - Trả về thông tin người dùng
     * - Xác thực thành công
     */
    @Test
    void authenticate_Success() {
        // Given
        when(userService.getUserByUsername(authRequest.getUsername())).thenReturn(Optional.of(mockUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(new UsernamePasswordAuthenticationToken(mockUser, null));

        // When
        AuthResponse response = authenticationService.authenticate(authRequest);

        // Then
        assertNotNull(response);
        assertEquals(TEST_TOKEN, response.getToken());
        assertEquals(mockUser.getId(), response.getUserId());
        assertEquals(mockUser.getUsername(), response.getUsername());
        assertEquals(mockUser.getRole(), response.getRole());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    /**
     * Test case: Đăng nhập thất bại - Thông tin đăng nhập không đúng
     * Tình huống: Người dùng đăng nhập với username hoặc password sai
     * Kết quả mong đợi: 
     * - Ném ra RuntimeException
     * - Không truy vấn thông tin người dùng
     */
    @Test
    void authenticate_BadCredentials() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Bad credentials"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            authenticationService.authenticate(authRequest);
        });
        verify(userService, never()).getUserByUsername(any());
    }

    /**
     * Test case: Đăng nhập thất bại - Không tìm thấy người dùng
     * Tình huống: Người dùng đăng nhập với username không tồn tại
     * Kết quả mong đợi: 
     * - Ném ra RuntimeException
     * - Xác thực thành công nhưng không tìm thấy người dùng
     */
    @Test
    void authenticate_UserNotFound() {
        // Given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(new UsernamePasswordAuthenticationToken(mockUser, null));
        when(userService.getUserByUsername(authRequest.getUsername())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            authenticationService.authenticate(authRequest);
        });
    }
} 
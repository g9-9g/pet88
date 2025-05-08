package com.framja.itss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framja.itss.RestDocsConfiguration;
import com.framja.itss.dto.AuthRequest;
import com.framja.itss.dto.AuthResponse;
import com.framja.itss.entity.User.RoleName;
import com.framja.itss.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@Import(RestDocsConfiguration.class)
public class AuthControllerDocumentation {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void loginTest() throws Exception {
        // Given
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("password");

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTYxNTIyNTQ3MywiZXhwIjoxNjE1MjI5MDczfQ.HZJSQnRFtF4Ygd5JMCK4KYyQ-Q5NJgz3NGL2Zo-hlTI");
        authResponse.setUsername("testuser");
        authResponse.setRole(RoleName.ROLE_ADMIN);

        given(authenticationService.authenticate(any(AuthRequest.class))).willReturn(authResponse);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("auth-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("username").description("The username for authentication"),
                                fieldWithPath("password").description("The password for authentication")
                        ),
                        responseFields(
                                fieldWithPath("token").description("JWT token for authentication"),
                                fieldWithPath("username").description("Username of the authenticated user"),
                                fieldWithPath("role").description("Role of the authenticated user"),
                                fieldWithPath("userId").description("ID of the authenticated user")
                        )
                ));
    }
} 
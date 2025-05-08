package com.framja.itss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framja.itss.RestDocsConfiguration;
import com.framja.itss.dto.pet.PetDto;
import com.framja.itss.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PetController.class)
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
@Import(RestDocsConfiguration.class)
public class PetControllerDocumentation {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getAllPetsTest() throws Exception {
        // Given
        PetDto pet1 = PetDto.builder()
            .petId(1L)
            .name("Fluffy")
            .species("Cat")
            .build();
        
        PetDto pet2 = PetDto.builder()
            .petId(2L)
            .name("Rex")
            .species("Dog")
            .build();
        
        List<PetDto> pets = Arrays.asList(pet1, pet2);
        
        given(petService.getAllPets()).willReturn(pets);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/pets")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-all-pets",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].petId").description("The unique ID of the pet"),
                                fieldWithPath("[].name").description("The name of the pet"),
                                fieldWithPath("[].species").description("The species of the pet"),
                                fieldWithPath("[].ownerId").optional().description("The ID of the pet owner")
                                // Document other fields as needed
                        )
                ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void getPetByIdTest() throws Exception {
        // Given
        PetDto pet = PetDto.builder()
            .petId(1L)
            .name("Fluffy")
            .species("Cat")
            .build();
        
        given(petService.getPetById(anyLong())).willReturn(Optional.of(pet));

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/pets/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("get-pet-by-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("The ID of the pet to retrieve")
                        ),
                        responseFields(
                                fieldWithPath("petId").description("The unique ID of the pet"),
                                fieldWithPath("name").description("The name of the pet"),
                                fieldWithPath("species").description("The species of the pet"),
                                fieldWithPath("ownerId").optional().description("The ID of the pet owner")
                                // Document other fields as needed
                        )
                ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createPetTest() throws Exception {
        // Given
        PetDto petToCreate = PetDto.builder()
            .name("Fluffy")
            .species("Cat")
            .ownerId(1L)
            .build();
        
        PetDto createdPet = PetDto.builder()
            .petId(1L)
            .name("Fluffy")
            .species("Cat")
            .ownerId(1L)
            .build();
        
        given(petService.createPet(any(PetDto.class))).willReturn(createdPet);

        // When & Then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(petToCreate))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("create-pet",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("The name of the pet"),
                                fieldWithPath("species").description("The species of the pet"),
                                fieldWithPath("ownerId").description("The ID of the pet owner"),
                                fieldWithPath("petId").optional().description("The ID of the pet (ignored for creation)"),
                                fieldWithPath("breed").optional().description("The breed of the pet"),
                                fieldWithPath("gender").optional().description("The gender of the pet"),
                                fieldWithPath("birthdate").optional().description("The birthdate of the pet"),
                                fieldWithPath("color").optional().description("The color of the pet"),
                                fieldWithPath("avatarUrl").optional().description("URL to the pet's avatar image"),
                                fieldWithPath("healthNotes").optional().description("Health notes about the pet"),
                                fieldWithPath("vaccinationHistory").optional().description("Vaccination history of the pet"),
                                fieldWithPath("nutritionNotes").optional().description("Nutrition notes for the pet"),
                                fieldWithPath("createdAt").optional().description("When the pet record was created"),
                                fieldWithPath("updatedAt").optional().description("When the pet record was last updated")
                        ),
                        responseFields(
                                fieldWithPath("petId").description("The unique ID of the created pet"),
                                fieldWithPath("name").description("The name of the pet"),
                                fieldWithPath("species").description("The species of the pet"),
                                fieldWithPath("ownerId").description("The ID of the pet owner"),
                                fieldWithPath("breed").optional().description("The breed of the pet"),
                                fieldWithPath("gender").optional().description("The gender of the pet"),
                                fieldWithPath("birthdate").optional().description("The birthdate of the pet"),
                                fieldWithPath("color").optional().description("The color of the pet"),
                                fieldWithPath("avatarUrl").optional().description("URL to the pet's avatar image"),
                                fieldWithPath("healthNotes").optional().description("Health notes about the pet"),
                                fieldWithPath("vaccinationHistory").optional().description("Vaccination history of the pet"),
                                fieldWithPath("nutritionNotes").optional().description("Nutrition notes for the pet"),
                                fieldWithPath("createdAt").optional().description("When the pet record was created"),
                                fieldWithPath("updatedAt").optional().description("When the pet record was last updated")
                        )
                ));
    }
} 
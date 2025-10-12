package com.smart.mart.catalog.infrastructure.adapter.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smart.mart.catalog.application.dto.request.CreateCategoryRequest;
import com.smart.mart.catalog.application.dto.request.UpdateCategoryRequest;
import com.smart.mart.catalog.application.dto.response.CategoryResponse;
import com.smart.mart.catalog.application.port.input.CreateCategoryUseCase;
import com.smart.mart.catalog.application.port.input.DeleteCategoryUseCase;
import com.smart.mart.catalog.application.port.input.GetCategoryUseCase;
import com.smart.mart.catalog.application.port.input.UpdateCategoryUseCase;
import com.smart.mart.catalog.domain.exception.CategoryNotFoundException;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private GetCategoryUseCase getCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    private CategoryResponse categoryResponse;
    private CreateCategoryRequest createRequest;

    @BeforeEach
    void setUp() {
        UUID categoryId = UUID.randomUUID();
        UUID orgId = UUID.randomUUID();

        categoryResponse = new CategoryResponse();
        categoryResponse.setId(categoryId);
        categoryResponse.setOrgId(orgId);
        categoryResponse.setName("Electronics");
        categoryResponse.setDescription("Electronic items");
        categoryResponse.setActive(true);

        createRequest = new CreateCategoryRequest();
        createRequest.setOrgId(orgId);
        createRequest.setName("Electronics");
        createRequest.setDescription("Electronic items");
    }

    @Test
    void shouldCreateCategory() throws Exception {
        // Given
        when(createCategoryUseCase.create(any(CreateCategoryRequest.class)))
                .thenReturn(categoryResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(categoryResponse.getId().toString()))
                .andExpect(jsonPath("$.name").value(categoryResponse.getName()));

        verify(createCategoryUseCase, times(1)).create(any(CreateCategoryRequest.class));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsMissing() throws Exception {
        // Given
        createRequest.setName(null);

        // When & Then
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());

        verify(createCategoryUseCase, never()).create(any(CreateCategoryRequest.class));
    }

    @Test
    void shouldGetCategoryById() throws Exception {
        // Given
        UUID categoryId = categoryResponse.getId();
        when(getCategoryUseCase.getById(categoryId)).thenReturn(categoryResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/categories/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryId.toString()))
                .andExpect(jsonPath("$.name").value(categoryResponse.getName()));
    }

    @Test
    void shouldReturn404WhenCategoryNotFound() throws Exception {
        // Given
        UUID categoryId = UUID.randomUUID();
        when(getCategoryUseCase.getById(categoryId))
                .thenThrow(new CategoryNotFoundException(new CategoryId(categoryId)));

        // When & Then
        mockMvc.perform(get("/api/v1/categories/{id}", categoryId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetCategoriesByOrg() throws Exception {
        // Given
        UUID orgId = UUID.randomUUID();
        List<CategoryResponse> responses = Arrays.asList(categoryResponse);
        when(getCategoryUseCase.getByOrgId(orgId)).thenReturn(responses);

        // When & Then
        mockMvc.perform(get("/api/v1/categories/organization/{orgId}", orgId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(categoryResponse.getId().toString()));
    }

    @Test
    void shouldUpdateCategory() throws Exception {
        // Given
        UUID categoryId = categoryResponse.getId();
        UpdateCategoryRequest updateRequest = new UpdateCategoryRequest();
        updateRequest.setName("Updated Electronics");

        when(updateCategoryUseCase.update(eq(categoryId), any(UpdateCategoryRequest.class)))
                .thenReturn(categoryResponse);

        // When & Then
        mockMvc.perform(put("/api/v1/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        // Given
        UUID categoryId = UUID.randomUUID();
        doNothing().when(deleteCategoryUseCase).delete(categoryId);

        // When & Then
        mockMvc.perform(delete("/api/v1/categories/{id}", categoryId))
                .andExpect(status().isNoContent());

        verify(deleteCategoryUseCase, times(1)).delete(categoryId);
    }

}

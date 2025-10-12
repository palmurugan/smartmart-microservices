package com.smart.mart.catalog.application.service;

import com.smart.mart.catalog.application.dto.request.CreateCategoryRequest;
import com.smart.mart.catalog.application.dto.response.CategoryResponse;
import com.smart.mart.catalog.application.mapper.CategoryMapper;
import com.smart.mart.catalog.application.port.output.CategoryRepository;
import com.smart.mart.catalog.domain.exception.CategoryNotFoundException;
import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.service.CategoryHierarchyValidator;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.common.domain.valueobject.OrganizationId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryHierarchyValidator hierarchyValidator;

    @InjectMocks
    private CreateCategoryService createCategoryService;

    private CreateCategoryRequest request;
    private Category category;
    private CategoryResponse response;

    @BeforeEach
    void setUp() {
        request = new CreateCategoryRequest();
        request.setOrgId(UUID.randomUUID());
        request.setName("Electronics");
        request.setDescription("Electronic items");

        category = Category.builder()
                .orgId(new OrganizationId(request.getOrgId()))
                .name(request.getName())
                .description(request.getDescription())
                .build();

        response = new CategoryResponse();
        response.setId(category.getId().getValue());
        response.setName(category.getName());
    }

    @Test
    void shouldCreateCategorySuccessfully() {
        // Given
        when(categoryMapper.toDomain(request)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(response);

        // When
        CategoryResponse result = createCategoryService.create(request);

        // Then
        assertNotNull(result);
        assertEquals(response.getName(), result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void shouldValidateParentExistsWhenProvided() {
        // Given
        UUID parentId = UUID.randomUUID();
        request.setParentId(parentId);

        when(categoryRepository.existsById(any(CategoryId.class))).thenReturn(false);

        // When & Then
        assertThrows(CategoryNotFoundException.class, () -> {
            createCategoryService.create(request);
        });

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void shouldCreateCategoryWithValidParent() {
        // Given
        UUID parentId = UUID.randomUUID();
        request.setParentId(parentId);

        when(categoryRepository.existsById(any(CategoryId.class))).thenReturn(true);
        when(categoryMapper.toDomain(request)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toResponse(category)).thenReturn(response);

        // When
        CategoryResponse result = createCategoryService.create(request);

        // Then
        assertNotNull(result);
        verify(categoryRepository, times(1)).existsById(any(CategoryId.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

}

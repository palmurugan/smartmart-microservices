package com.smart.mart.catalog.domain.model;

import com.smart.mart.catalog.domain.exception.InvalidCategoryException;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.common.domain.valueobject.OrganizationId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    void shouldCreateValidCategory() {
        // Given
        OrganizationId orgId = new OrganizationId(UUID.randomUUID());
        String name = "Electronics";

        // When
        Category category = Category.builder()
                .orgId(orgId)
                .name(name)
                .description("Electronic items")
                .build();

        // Then
        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(name, category.getName());
        assertTrue(category.isActive());
        assertTrue(category.isRoot());
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        // Given
        OrganizationId orgId = new OrganizationId(UUID.randomUUID());

        // When & Then
        assertThrows(InvalidCategoryException.class, () -> {
            Category.builder()
                    .orgId(orgId)
                    .name(null)
                    .build();
        });
    }

    @Test
    void shouldThrowExceptionWhenNameExceedsMaxLength() {
        // Given
        OrganizationId orgId = new OrganizationId(UUID.randomUUID());
        String longName = "a".repeat(256);

        // When & Then
        assertThrows(InvalidCategoryException.class, () -> {
            Category.builder()
                    .orgId(orgId)
                    .name(longName)
                    .build();
        });
    }

    @Test
    void shouldThrowExceptionWhenNameContainsInvalidCharacters() {
        // Given
        OrganizationId orgId = new OrganizationId(UUID.randomUUID());

        // When & Then
        assertThrows(InvalidCategoryException.class, () -> {
            Category.builder()
                    .orgId(orgId)
                    .name("Category@#$%")
                    .build();
        });
    }

    @Test
    void shouldThrowExceptionWhenCategoryIsItsOwnParent() {
        // Given
        CategoryId categoryId = CategoryId.generate();
        OrganizationId orgId = new OrganizationId(UUID.randomUUID());

        // When & Then
        assertThrows(InvalidCategoryException.class, () -> {
            Category.builder()
                    .id(categoryId)
                    .orgId(orgId)
                    .parentId(categoryId)
                    .name("Test")
                    .build();
        });
    }

    @Test
    void shouldActivateCategory() {
        // Given
        Category category = createValidCategory();
        category.deactivate();

        // When
        category.activate();

        // Then
        assertTrue(category.isActive());
    }

    @Test
    void shouldUpdateNameSuccessfully() {
        // Given
        Category category = createValidCategory();
        String newName = "Updated Category";

        // When
        category.updateName(newName);

        // Then
        assertEquals(newName, category.getName());
    }

    @Test
    void shouldRollbackNameUpdateOnValidationFailure() {
        // Given
        Category category = createValidCategory();
        String originalName = category.getName();
        String invalidName = "Invalid@Name";

        // When & Then
        assertThrows(InvalidCategoryException.class, () -> {
            category.updateName(invalidName);
        });

        // Name should remain unchanged
        assertEquals(originalName, category.getName());
    }

    @Test
    void shouldMoveToParent() {
        // Given
        Category category = createValidCategory();
        CategoryId newParentId = CategoryId.generate();

        // When
        category.moveToParent(newParentId);

        // Then
        assertEquals(newParentId, category.getParentId());
        assertFalse(category.isRoot());
    }

    @Test
    void shouldUpdateMetadata() {
        // Given
        Category category = createValidCategory();

        // When
        category.updateMetadata("color", "blue");
        category.updateMetadata("priority", 1);

        // Then
        assertNotNull(category.getMetadata());
        assertEquals("blue", category.getMetadata().getAttribute("color"));
        assertEquals(1, category.getMetadata().getAttribute("priority"));
    }

    private Category createValidCategory() {
        return Category.builder()
                .orgId(new OrganizationId(UUID.randomUUID()))
                .name("Test Category")
                .description("Test Description")
                .build();
    }
}

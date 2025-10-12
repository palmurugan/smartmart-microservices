package com.smart.mart.catalog.domain.service;

import com.smart.mart.catalog.domain.exception.CircularReferenceException;
import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.common.domain.valueobject.OrganizationId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryHierarchyValidatorTest {

    private CategoryHierarchyValidator validator;
    private Map<CategoryId, Category> categoryStore;

    @BeforeEach
    void setUp() {
        validator = new CategoryHierarchyValidator();
        categoryStore = new HashMap<>();
    }

    @Test
    void shouldAllowMovingToNullParent() {
        // Given
        CategoryId categoryId = CategoryId.generate();

        // When & Then - should not throw exception
        assertDoesNotThrow(() -> {
            validator.validateNoCircularReference(
                    categoryId,
                    null,
                    this::getCategory
            );
        });
    }

    @Test
    void shouldThrowExceptionWhenCategoryIsItsOwnParent() {
        // Given
        CategoryId categoryId = CategoryId.generate();

        // When & Then
        assertThrows(CircularReferenceException.class, () -> {
            validator.validateNoCircularReference(
                    categoryId,
                    categoryId,
                    this::getCategory
            );
        });
    }

    @Test
    void shouldThrowExceptionWhenCreatingCircularReference() {
        // Given: A -> B -> C
        OrganizationId orgId = new OrganizationId(UUID.randomUUID());

        CategoryId categoryAId = CategoryId.generate();
        CategoryId categoryBId = CategoryId.generate();
        CategoryId categoryCId = CategoryId.generate();

        Category categoryA = Category.builder()
                .id(categoryAId)
                .orgId(orgId)
                .name("A")
                .build();

        Category categoryB = Category.builder()
                .id(categoryBId)
                .orgId(orgId)
                .parentId(categoryAId)
                .name("B")
                .build();

        Category categoryC = Category.builder()
                .id(categoryCId)
                .orgId(orgId)
                .parentId(categoryBId)
                .name("C")
                .build();

        categoryStore.put(categoryAId, categoryA);
        categoryStore.put(categoryBId, categoryB);
        categoryStore.put(categoryCId, categoryC);

        // When & Then: Trying to move A under C (would create A -> B -> C -> A)
        assertThrows(CircularReferenceException.class, () -> {
            validator.validateNoCircularReference(
                    categoryAId,
                    categoryCId,
                    this::getCategory
            );
        });
    }

    @Test
    void shouldAllowValidHierarchyMove() {
        // Given: A -> B and C (separate)
        OrganizationId orgId = new OrganizationId(UUID.randomUUID());

        CategoryId categoryAId = CategoryId.generate();
        CategoryId categoryBId = CategoryId.generate();
        CategoryId categoryCId = CategoryId.generate();

        Category categoryA = Category.builder()
                .id(categoryAId)
                .orgId(orgId)
                .name("A")
                .build();

        Category categoryB = Category.builder()
                .id(categoryBId)
                .orgId(orgId)
                .parentId(categoryAId)
                .name("B")
                .build();

        Category categoryC = Category.builder()
                .id(categoryCId)
                .orgId(orgId)
                .name("C")
                .build();

        categoryStore.put(categoryAId, categoryA);
        categoryStore.put(categoryBId, categoryB);
        categoryStore.put(categoryCId, categoryC);

        // When & Then: Moving C under B (valid: A -> B -> C)
        assertDoesNotThrow(() -> {
            validator.validateNoCircularReference(
                    categoryCId,
                    categoryBId,
                    this::getCategory
            );
        });
    }

    private Category getCategory(CategoryId categoryId) {
        return categoryStore.get(categoryId);
    }
}

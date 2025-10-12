package com.smart.mart.catalog.application.service;

import com.smart.mart.catalog.application.port.input.DeleteCategoryUseCase;
import com.smart.mart.catalog.application.port.output.CategoryRepository;
import com.smart.mart.catalog.domain.exception.CategoryNotFoundException;
import com.smart.mart.catalog.domain.exception.InvalidCategoryException;
import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.common.config.annotation.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class DeleteCategoryService implements DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;

    @Override
    public void delete(UUID id) {
        CategoryId categoryId = new CategoryId(id);

        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException("Category not found with id: " + id);
        }

        List<Category> children = categoryRepository.findByParentId(categoryId);

        if (!children.isEmpty()) {
            throw new InvalidCategoryException("Cannot delete category with children.");
        }

        categoryRepository.delete(categoryId);
    }
}

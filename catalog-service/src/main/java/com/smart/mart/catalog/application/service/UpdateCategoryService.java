package com.smart.mart.catalog.application.service;

import com.smart.mart.catalog.application.dto.request.UpdateCategoryRequest;
import com.smart.mart.catalog.application.dto.response.CategoryResponse;
import com.smart.mart.catalog.application.mapper.CategoryMapper;
import com.smart.mart.catalog.application.port.input.UpdateCategoryUseCase;
import com.smart.mart.catalog.application.port.output.CategoryRepository;
import com.smart.mart.catalog.domain.exception.CategoryNotFoundException;
import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.common.config.annotation.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class UpdateCategoryService implements UpdateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse update(UUID id, UpdateCategoryRequest request) {
        CategoryId categoryId = new CategoryId(id);

        //Load existing category
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));

        if (request.getName() != null) {
            category.updateName(request.getName());
        }

        if (request.getDescription() != null) {
            category.updateDescription(request.getDescription());
        }

        if (request.getParentId() != null) {
            CategoryId newParentId = new CategoryId(request.getParentId());
            category.moveToParent(newParentId);
        }

        if (request.getMetadata() != null) {
            request.getMetadata().forEach(category::updateMetadata);
        }

        if (request.getIsActive() != null) {
            if (request.getIsActive()) {
                category.activate();
            } else {
                category.deactivate();
            }
        }
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toResponse(updatedCategory);
    }
}

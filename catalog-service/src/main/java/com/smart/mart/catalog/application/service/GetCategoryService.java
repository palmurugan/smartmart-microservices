package com.smart.mart.catalog.application.service;

import com.smart.mart.catalog.application.dto.response.CategoryResponse;
import com.smart.mart.catalog.application.mapper.CategoryMapper;
import com.smart.mart.catalog.application.port.input.GetCategoryUseCase;
import com.smart.mart.catalog.application.port.output.CategoryRepository;
import com.smart.mart.catalog.domain.exception.CategoryNotFoundException;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.common.config.annotation.UseCase;
import com.smart.mart.common.domain.valueobject.OrganizationId;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetCategoryService implements GetCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponse getById(UUID id) {
        return categoryRepository.findById(new CategoryId(id))
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    @Override
    public List<CategoryResponse> getByOrgId(UUID orgId) {
        return categoryRepository.findByOrgId(new OrganizationId(orgId))
                .stream().map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public List<CategoryResponse> getChildren(UUID parentId) {
        return categoryRepository.findByParentId(new CategoryId(parentId))
                .stream().map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    public List<CategoryResponse> getRootCategories(UUID orgId) {
        return categoryRepository.findRootCategories(new OrganizationId(orgId))
                .stream().map(categoryMapper::toResponse)
                .toList();
    }
}

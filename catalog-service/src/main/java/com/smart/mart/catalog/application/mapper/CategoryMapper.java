package com.smart.mart.catalog.application.mapper;

import com.smart.mart.catalog.application.dto.request.CreateCategoryRequest;
import com.smart.mart.catalog.application.dto.response.CategoryResponse;
import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.catalog.domain.valueobject.Metadata;
import com.smart.mart.common.domain.valueobject.OrganizationId;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toDomain(CreateCategoryRequest request) {
        Category.Builder builder = Category.builder()
                .orgId(new OrganizationId(request.getOrgId()))
                .name(request.getName())
                .description(request.getDescription());

        if (request.getParentId() != null) {
            builder.parentId(new CategoryId(request.getParentId()));
        }

        if (request.getMetadata() != null && !request.getMetadata().isEmpty()) {
            builder.metadata(new Metadata(request.getMetadata()));
        }

        return builder.build();
    }

    public CategoryResponse toResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId().getValue());
        response.setOrgId(category.getOrgId().getValue());
        if (category.getParentId() != null) {
            response.setParentId(category.getParentId().getValue());
        }
        response.setName(category.getName());
        response.setDescription(category.getDescription());

        if (category.getMetadata() != null) {
            response.setMetadata(category.getMetadata().getAttributes());
        }

        response.setCreatedAt(category.getCreatedAt());
        response.setUpdatedAt(category.getUpdatedAt());
        response.setActive(category.isActive());
        return response;
    }
}

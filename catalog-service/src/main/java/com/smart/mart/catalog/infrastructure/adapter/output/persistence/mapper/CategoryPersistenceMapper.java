package com.smart.mart.catalog.infrastructure.adapter.output.persistence.mapper;

import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.catalog.domain.valueobject.Metadata;
import com.smart.mart.catalog.infrastructure.adapter.output.persistence.entity.CategoryEntity;
import com.smart.mart.common.domain.valueobject.OrganizationId;
import org.springframework.stereotype.Component;

@Component
public class CategoryPersistenceMapper {

    public CategoryEntity toEntity(Category category) {
        if (category == null) {
            return null;
        }
        CategoryEntity entity = new CategoryEntity();

        // Map Value Objects to primitives
        entity.setId(category.getId().getValue());
        entity.setOrgId(category.getOrgId().getValue());

        if (category.getParentId() != null) {
            entity.setParentId(category.getParentId().getValue());
        }

        entity.setName(category.getName());
        entity.setDescription(category.getDescription());

        // Map Metadata Value Object to Map
        if (category.getMetadata() != null) {
            entity.setMetadata(category.getMetadata().getAttributes());
        }

        entity.setCreatedAt(category.getCreatedAt());
        entity.setUpdatedAt(category.getUpdatedAt());
        entity.setIsActive(category.isActive());
        return entity;
    }

    public Category toDomain(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }

        // Reconstruct Value Objects from primitives
        CategoryId categoryId = new CategoryId(entity.getId());
        OrganizationId orgId = new OrganizationId(entity.getOrgId());
        CategoryId parentId = entity.getParentId() != null
                ? new CategoryId(entity.getParentId())
                : null;

        Metadata metadata = entity.getMetadata() != null
                ? new Metadata(entity.getMetadata())
                : new Metadata();

        // Build Domain Model using Builder
        return Category.builder()
                .id(categoryId)
                .orgId(orgId)
                .parentId(parentId)
                .name(entity.getName())
                .description(entity.getDescription())
                .metadata(metadata)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .isActive(entity.getIsActive())
                .build();
    }

    public void updateEntity(Category category, CategoryEntity entity) {
        entity.setName(category.getName());
        entity.setDescription(category.getDescription());

        if (category.getParentId() != null) {
            entity.setParentId(category.getParentId().getValue());
        } else {
            entity.setParentId(null);
        }

        if (category.getMetadata() != null) {
            entity.setMetadata(category.getMetadata().getAttributes());
        }

        entity.setUpdatedAt(category.getUpdatedAt());
        entity.setIsActive(category.isActive());
    }
}

package com.smart.mart.catalog.infrastructure.adapter.output.persistence;

import com.smart.mart.catalog.application.port.output.CategoryRepository;
import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.catalog.infrastructure.adapter.output.persistence.entity.CategoryEntity;
import com.smart.mart.catalog.infrastructure.adapter.output.persistence.mapper.CategoryPersistenceMapper;
import com.smart.mart.catalog.infrastructure.adapter.output.persistence.repository.CategoryJpaRepository;
import com.smart.mart.common.domain.valueobject.OrganizationId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;
    private final CategoryPersistenceMapper categoryPersistenceMapper;

    @Override
    public Category save(Category category) {
        // Check if it's an update or create
        Optional<CategoryEntity> existingEntity = categoryJpaRepository.findById(category.getId().getValue());

        CategoryEntity entity;
        if (existingEntity.isPresent()) {
            entity = existingEntity.get();
            categoryPersistenceMapper.updateEntity(category, entity);
        } else {
            entity = categoryPersistenceMapper.toEntity(category);
        }

        CategoryEntity savedEntity = categoryJpaRepository.save(entity);
        return categoryPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Category> findById(CategoryId id) {
        return categoryJpaRepository.findById(id.getValue())
                .map(categoryPersistenceMapper::toDomain);
    }

    @Override
    public List<Category> findByOrgId(OrganizationId orgId) {
        return categoryJpaRepository.findByOrgId(orgId.getValue())
                .stream()
                .map(categoryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findByParentId(CategoryId parentId) {
        return categoryJpaRepository.findByParentId(parentId.getValue())
                .stream()
                .map(categoryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Category> findRootCategories(OrganizationId orgId) {
        return categoryJpaRepository.findRootCategories(orgId.getValue())
                .stream()
                .map(categoryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(CategoryId id) {
        categoryJpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(CategoryId id) {
        return categoryJpaRepository.existsById(id.getValue());
    }
}

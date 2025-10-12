package com.smart.mart.catalog.application.port.output;

import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.common.domain.valueobject.OrganizationId;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);

    Optional<Category> findById(CategoryId id);

    List<Category> findByOrgId(OrganizationId orgId);

    List<Category> findByParentId(CategoryId parentId);

    List<Category> findRootCategories(OrganizationId orgId);

    void delete(CategoryId id);

    boolean existsById(CategoryId id);
}

package com.smart.mart.catalog.domain.service;

import com.smart.mart.catalog.domain.exception.CircularReferenceException;
import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.valueobject.CategoryId;

import java.util.HashSet;
import java.util.Set;

public class CategoryHierarchyValidator {

    public void validateNoCircularReference(
            CategoryId categoryId,
            CategoryId newParentId,
            CategoryHierarchyChecker hierarchyChecker) {
        if (newParentId == null) {
            return;
        }

        if(categoryId.equals(newParentId)) {
            throw new CircularReferenceException("Category cannot be its own parent");
        }

        // Check if the new parent is a descendant of the category being moved
        if (isDescendant(newParentId, categoryId, hierarchyChecker, new HashSet<>())) {
            throw new CircularReferenceException(
                    "Cannot move category under its own descendant - this would create a circular reference"
            );
        }
    }

    private boolean isDescendant(
            CategoryId potentialDescendant,
            CategoryId ancestorId,
            CategoryHierarchyChecker hierarchyChecker,
            Set<CategoryId> visited
    ) {
        // Prevent infinite loops
        if (visited.contains(potentialDescendant)) {
            return false;
        }
        visited.add(potentialDescendant);

        Category category = hierarchyChecker.getCategory(potentialDescendant);

        if (category == null || !category.hasParent()) {
            return false;
        }

        if (category.getParentId().equals(ancestorId)) {
            return true;
        }

        return isDescendant(category.getParentId(), ancestorId, hierarchyChecker, visited);
    }

    public interface CategoryHierarchyChecker {
        Category getCategory(CategoryId categoryId);
    }
}

package com.smart.mart.catalog.domain.model;

import com.smart.mart.catalog.domain.exception.InvalidCategoryException;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.catalog.domain.valueobject.Metadata;
import com.smart.mart.common.domain.valueobject.OrganizationId;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class Category {
    private CategoryId id;
    private OrganizationId orgId;
    private CategoryId parentId;
    private String name;
    private String description;
    private Metadata metadata;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private boolean isActive;

    private Category(Builder builder) {
        this.id = builder.id;
        this.orgId = builder.orgId;
        this.parentId = builder.parentId;
        this.name = builder.name;
        this.description = builder.description;
        this.metadata = builder.metadata;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.isActive = builder.isActive;

        this.validate();
    }

    private void validate() {
        validateName();
        validateOrganization();
        validateParentReference();
        validateDescription();
    }

    private void validateName() {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidCategoryException("Category name cannot be null or empty");
        }

        if (name.length() > 255) {
            throw new InvalidCategoryException("Category name cannot be more than 255 characters");
        }

        // Business rule: Name should not contain special characters except hyphen and underscore
        if (!name.matches("^[a-zA-Z0-9\\s\\-_]+$")) {
            throw new InvalidCategoryException(
                    "Category name can only contain alphanumeric characters, spaces, hyphens, and underscores"
            );
        }
    }

    private void validateOrganization() {
        if (orgId == null) {
            throw new InvalidCategoryException("Organization ID cannot be null");
        }
    }

    private void validateParentReference() {
        // Business rule: Category cannot be its own parent
        if (parentId != null && parentId.equals(id)) {
            throw new InvalidCategoryException("Category cannot be its own parent");
        }
    }

    private void validateDescription() {
        if (description != null && description.length() > 1000) {
            throw new InvalidCategoryException("Description cannot exceed 1000 characters");
        }
    }

    public void activate() {
        this.isActive = true;
        this.updatedAt = ZonedDateTime.now();
    }

    public void deactivate() {
        this.isActive = false;
        this.updatedAt = ZonedDateTime.now();
    }

    public void updateName(String newName) {
        String oldName = this.name;
        this.name = newName;
        this.updatedAt = ZonedDateTime.now();
        try {
            validateName();
        } catch (InvalidCategoryException e) {
            this.name = oldName;
            throw e;
        }
    }

    public void updateDescription(String newDescription) {
        String oldDescription = this.description;
        this.description = newDescription;
        this.updatedAt = ZonedDateTime.now();
        try {
            validateDescription();
        } catch (InvalidCategoryException e) {
            this.description = oldDescription;
            throw e;
        }
    }

    public void updateMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new Metadata();
        }
        this.metadata.addAttribute(key, value);
        this.updatedAt = ZonedDateTime.now();
    }

    public void moveToParent(CategoryId newParentId) {
        if (newParentId != null && newParentId.equals(this.id)) {
            throw new InvalidCategoryException("Cannot move category to itself");
        }
    }

    public boolean isRoot() {
        return parentId == null;
    }

    public boolean hasParent() {
        return parentId != null;
    }

    public static class Builder {
        private CategoryId id;
        private OrganizationId orgId;
        private CategoryId parentId;
        private String name;
        private String description;
        private Metadata metadata;
        private ZonedDateTime createdAt;
        private ZonedDateTime updatedAt;
        private boolean isActive;

        public Builder id(CategoryId id) {
            this.id = id;
            return this;
        }

        public Builder orgId(OrganizationId orgId) {
            this.orgId = orgId;
            return this;
        }

        public Builder parentId(CategoryId parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder metadata(Metadata metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder createdAt(ZonedDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(ZonedDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Category build() {
            if (id == null) {
                id = CategoryId.generate();
            }
            if (createdAt == null) {
                createdAt = ZonedDateTime.now();
            }
            if (updatedAt == null) {
                updatedAt = ZonedDateTime.now();
            }
            if (metadata == null) {
                metadata = new Metadata();
            }
            return new Category(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

}

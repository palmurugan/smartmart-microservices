package com.smart.mart.catalog.domain.exception;

import com.smart.mart.catalog.domain.valueobject.CategoryId;

public class CategoryNotFoundException extends DomainException {

    public CategoryNotFoundException(CategoryId categoryId) {
        super("Category not found with id: " + categoryId.getValue());
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}

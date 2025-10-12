package com.smart.mart.catalog.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public class CategoryId {

    private final UUID value;

    public CategoryId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("CategoryId cannot be null");
        }
        this.value = value;
    }

    public CategoryId(String value) {
        this(UUID.fromString(value));
    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryId that = (CategoryId) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

}

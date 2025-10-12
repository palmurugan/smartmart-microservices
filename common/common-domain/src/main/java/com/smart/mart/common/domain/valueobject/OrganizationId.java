package com.smart.mart.common.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public class OrganizationId {
    private final UUID value;

    public OrganizationId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("OrganizationId cannot be null");
        }
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrganizationId that = (OrganizationId) o;
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

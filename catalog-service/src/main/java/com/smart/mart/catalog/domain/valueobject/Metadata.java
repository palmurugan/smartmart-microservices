package com.smart.mart.catalog.domain.valueobject;

import java.util.HashMap;
import java.util.Map;

public class Metadata {
    private final Map<String, Object> attributes;

    public Metadata() {
        this.attributes = new HashMap<>();
    }

    public Metadata(Map<String, Object> attributes) {
        this.attributes = new HashMap<>(attributes != null ? attributes : new HashMap<>());
    }

    public void addAttribute(String key, Object value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("Metadata key cannot be null or empty");
        }
        this.attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return this.attributes.get(key);
    }

    public Map<String, Object> getAttributes() {
        return new HashMap<>(attributes);
    }

    public boolean isEmpty() {
        return attributes.isEmpty();
    }

}

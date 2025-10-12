package com.smart.mart.catalog.infrastructure.adapter.output.persistence.entity;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Table(name = "category")
public class CategoryEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "org_id", columnDefinition = "uuid")
    private UUID orgId;

    @Column(name = "parent_id", columnDefinition = "uuid")
    private UUID parentId;

    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    @Column(name = "description", columnDefinition = "varchar")
    private String description;

    @Type(JsonBinaryType.class)
    @Column(name = "metadata", columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    @Column(name = "created_at", nullable = false, columnDefinition = "timestamptz")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    private ZonedDateTime updatedAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

}

package com.smart.mart.catalog.infrastructure.adapter.output.persistence.repository;

import com.smart.mart.catalog.infrastructure.adapter.output.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {

    List<CategoryEntity> findByOrgId(UUID orgId);

    List<CategoryEntity> findByParentId(UUID parentId);

    @Query("SELECT c FROM CategoryEntity c WHERE c.orgId = :orgId AND c.parentId IS NULL")
    List<CategoryEntity> findRootCategories(@Param("orgId") UUID orgId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CategoryEntity c WHERE c.parentId = :parentId")
    boolean hasChildren(@Param("parentId") UUID parentId);
}

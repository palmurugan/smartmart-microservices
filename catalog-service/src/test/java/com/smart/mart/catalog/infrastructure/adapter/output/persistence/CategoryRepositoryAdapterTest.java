package com.smart.mart.catalog.infrastructure.adapter.output.persistence;

import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.catalog.domain.valueobject.Metadata;
import com.smart.mart.catalog.infrastructure.adapter.output.persistence.entity.CategoryEntity;
import com.smart.mart.catalog.infrastructure.adapter.output.persistence.mapper.CategoryPersistenceMapper;
import com.smart.mart.catalog.infrastructure.adapter.output.persistence.repository.CategoryJpaRepository;
import com.smart.mart.common.domain.valueobject.OrganizationId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//(properties = {
//        "spring.datasource.url=jdbc:h2:mem:testdb",
//        "spring.jpa.hibernate.ddl-auto=create-drop"
//})

@DataJpaTest
@ActiveProfiles("test")
@EntityScan(basePackages = "com.smart.mart.catalog.infrastructure.adapter.output.persistence.entity")
@Import({CategoryRepositoryAdapter.class, CategoryPersistenceMapper.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryAdapterTest {

    @Autowired
    private CategoryRepositoryAdapter repositoryAdapter;

    @Autowired
    private CategoryJpaRepository jpaRepository;

    @Test
    void shouldSaveCategory() {
        // Given
        OrganizationId orgId = new OrganizationId(UUID.randomUUID());
        Map<String, Object> metadataMap = new HashMap<>();
        metadataMap.put("color", "blue");

        Category category = Category.builder()
                .orgId(orgId)
                .name("Electronics")
                .description("Electronic items")
                .metadata(new Metadata(metadataMap))
                .build();

        // When
        Category saved = repositoryAdapter.save(category);

        // Then
        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals(category.getName(), saved.getName());
        assertEquals(category.getOrgId(), saved.getOrgId());
    }

    @Test
    void shouldFindCategoryById() {
        // Given
        CategoryEntity entity = createAndSaveEntity();
        CategoryId categoryId = new CategoryId(entity.getId());

        // When
        Optional<Category> found = repositoryAdapter.findById(categoryId);

        // Then
        assertTrue(found.isPresent());
        assertEquals(entity.getName(), found.get().getName());
    }

    @Test
    void shouldFindCategoriesByOrgId() {
        // Given
        UUID orgId = UUID.randomUUID();
        createAndSaveEntity(orgId, "Category 1");
        createAndSaveEntity(orgId, "Category 2");

        // When
        List<Category> categories = repositoryAdapter.findByOrgId(new OrganizationId(orgId));

        // Then
        assertEquals(2, categories.size());
    }

    @Test
    void shouldFindRootCategories() {
        // Given
        UUID orgId = UUID.randomUUID();

        // Create root categories
        createAndSaveEntity(orgId, "Root 1", null);
        createAndSaveEntity(orgId, "Root 2", null);

        // Create child category
        CategoryEntity parent = createAndSaveEntity(orgId, "Parent", null);
        createAndSaveEntity(orgId, "Child", parent.getId());

        // When
        List<Category> rootCategories = repositoryAdapter.findRootCategories(new OrganizationId(orgId));

        // Then
        assertEquals(3, rootCategories.size());
        assertTrue(rootCategories.stream().allMatch(Category::isRoot));
    }

    @Test
    void shouldFindChildrenByParentId() {
        // Given
        UUID orgId = UUID.randomUUID();
        CategoryEntity parent = createAndSaveEntity(orgId, "Parent", null);
        createAndSaveEntity(orgId, "Child 1", parent.getId());
        createAndSaveEntity(orgId, "Child 2", parent.getId());

        // When
        List<Category> children = repositoryAdapter.findByParentId(new CategoryId(parent.getId()));

        // Then
        assertEquals(2, children.size());
        assertTrue(children.stream().allMatch(c -> c.getParentId() != null));
    }

    @Test
    void shouldDeleteCategory() {
        // Given
        CategoryEntity entity = createAndSaveEntity();
        CategoryId categoryId = new CategoryId(entity.getId());

        // When
        repositoryAdapter.delete(categoryId);

        // Then
        Optional<Category> found = repositoryAdapter.findById(categoryId);
        assertFalse(found.isPresent());
    }

    @Test
    void shouldCheckIfCategoryExists() {
        // Given
        CategoryEntity entity = createAndSaveEntity();
        CategoryId categoryId = new CategoryId(entity.getId());

        // When
        boolean exists = repositoryAdapter.existsById(categoryId);

        // Then
        assertTrue(exists);
    }

    private CategoryEntity createAndSaveEntity() {
        return createAndSaveEntity(UUID.randomUUID(), "Test Category", null);
    }

    private CategoryEntity createAndSaveEntity(UUID orgId, String name) {
        return createAndSaveEntity(orgId, name, null);
    }

    private CategoryEntity createAndSaveEntity(UUID orgId, String name, UUID parentId) {
        CategoryEntity entity = new CategoryEntity();
        entity.setId(UUID.randomUUID());
        entity.setOrgId(orgId);
        entity.setParentId(parentId);
        entity.setName(name);
        entity.setDescription("Test description");
        entity.setIsActive(true);
        entity.setCreatedAt(java.time.ZonedDateTime.now());
        entity.setUpdatedAt(java.time.ZonedDateTime.now());
        return jpaRepository.save(entity);
    }
}

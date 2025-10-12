package com.smart.mart.catalog.application.service;

import com.smart.mart.catalog.application.port.input.DeleteCategoryUseCase;
import com.smart.mart.catalog.application.port.output.CategoryRepository;
import com.smart.mart.catalog.application.port.output.messaging.EventPublisher;
import com.smart.mart.catalog.domain.event.CategoryDeletedEvent;
import com.smart.mart.catalog.domain.exception.CategoryNotFoundException;
import com.smart.mart.catalog.domain.exception.InvalidCategoryException;
import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.common.config.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class DeleteCategoryService implements DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public void delete(UUID id) {
        CategoryId categoryId = new CategoryId(id);

        // Load category to get org_id for event
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + categoryId));

        List<Category> children = categoryRepository.findByParentId(categoryId);

        if (!children.isEmpty()) {
            throw new InvalidCategoryException("Cannot delete category with children.");
        }

        categoryRepository.delete(categoryId);
        log.info("Category deleted with id: {}", id);

        // Publish event
        publishCategoryDeletedEvent(category);
    }

    private void publishCategoryDeletedEvent(Category category) {
        try {
            CategoryDeletedEvent event = new CategoryDeletedEvent(category.getId().getValue(), category.getOrgId().getValue());
            eventPublisher.publish(event);
            log.info("Published CategoryDeletedEvent for category id: {}", category.getId().getValue());
        } catch (Exception e) {
            log.error("Failed to publish CategoryDeletedEvent for category id: {}", category.getId().getValue(), e);
        }
    }
}

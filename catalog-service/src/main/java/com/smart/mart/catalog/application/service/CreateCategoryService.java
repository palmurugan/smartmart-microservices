package com.smart.mart.catalog.application.service;

import com.smart.mart.catalog.application.dto.request.CreateCategoryRequest;
import com.smart.mart.catalog.application.dto.response.CategoryResponse;
import com.smart.mart.catalog.application.mapper.CategoryMapper;
import com.smart.mart.catalog.application.port.input.CreateCategoryUseCase;
import com.smart.mart.catalog.application.port.output.CategoryRepository;
import com.smart.mart.catalog.application.port.output.messaging.EventPublisher;
import com.smart.mart.catalog.domain.event.CategoryCreatedEvent;
import com.smart.mart.catalog.domain.exception.CategoryNotFoundException;
import com.smart.mart.catalog.domain.model.Category;
import com.smart.mart.catalog.domain.valueobject.CategoryId;
import com.smart.mart.common.config.annotation.UseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Log4j2
public class CreateCategoryService implements CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventPublisher eventPublisher;

    @Override
    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        if (request.getParentId() != null) {
            validateParentExists(request.getParentId());
        }

        // Map DTO to Domain
        Category category = categoryMapper.toDomain(request);

        // Domain validation happens automatically in Category constructor
        Category savedCategory = categoryRepository.save(category);

        // Publish event
        publishCategoryCreatedEvent(savedCategory);

        // Map Domain to Response
        return categoryMapper.toResponse(savedCategory);
    }

    private void validateParentExists(java.util.UUID parentId) {
        if (!categoryRepository.existsById(new CategoryId(parentId))) {
            throw new CategoryNotFoundException("Parent category not found with id: " + parentId);
        }
    }

    private void publishCategoryCreatedEvent(Category category) {
        try {
            CategoryCreatedEvent event = new CategoryCreatedEvent(category);
            eventPublisher.publish(event);
            log.info("Published CategoryCreatedEvent for category id: {}", category.getId().getValue());
        } catch (Exception e) {
            log.error("Failed to publish CategoryCreatedEvent for category id: {}",
                    category.getId().getValue(), e);
        }
    }
}

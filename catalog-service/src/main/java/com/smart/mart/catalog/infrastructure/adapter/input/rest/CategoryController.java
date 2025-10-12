package com.smart.mart.catalog.infrastructure.adapter.input.rest;

import com.smart.mart.catalog.application.dto.request.CreateCategoryRequest;
import com.smart.mart.catalog.application.dto.response.CategoryResponse;
import com.smart.mart.catalog.application.port.input.CreateCategoryUseCase;
import com.smart.mart.catalog.application.port.input.GetCategoryUseCase;
import com.smart.mart.catalog.application.port.input.UpdateCategoryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Log4j2
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final GetCategoryUseCase getCategoryUseCase;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        log.info("Rest request to create category: {}", request);
        return ResponseEntity.ok(createCategoryUseCase.create(request));
    }
}

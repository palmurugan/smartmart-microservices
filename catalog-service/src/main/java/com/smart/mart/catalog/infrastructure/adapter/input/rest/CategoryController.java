package com.smart.mart.catalog.infrastructure.adapter.input.rest;

import com.smart.mart.catalog.application.dto.request.CreateCategoryRequest;
import com.smart.mart.catalog.application.dto.request.UpdateCategoryRequest;
import com.smart.mart.catalog.application.dto.response.CategoryResponse;
import com.smart.mart.catalog.application.port.input.CreateCategoryUseCase;
import com.smart.mart.catalog.application.port.input.DeleteCategoryUseCase;
import com.smart.mart.catalog.application.port.input.GetCategoryUseCase;
import com.smart.mart.catalog.application.port.input.UpdateCategoryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Log4j2
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final GetCategoryUseCase getCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        log.info("Rest request to create category: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createCategoryUseCase.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable UUID id) {
        CategoryResponse response = getCategoryUseCase.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organization/{orgId}")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByOrg(@PathVariable UUID orgId) {
        List<CategoryResponse> response = getCategoryUseCase.getByOrgId(orgId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<CategoryResponse>> getChildren(@PathVariable UUID parentId) {
        List<CategoryResponse> response = getCategoryUseCase.getChildren(parentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/organization/{orgId}/roots")
    public ResponseEntity<List<CategoryResponse>> getRootCategories(@PathVariable UUID orgId) {
        List<CategoryResponse> response = getCategoryUseCase.getRootCategories(orgId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCategoryRequest request
    ) {
        CategoryResponse response = updateCategoryUseCase.update(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        deleteCategoryUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}

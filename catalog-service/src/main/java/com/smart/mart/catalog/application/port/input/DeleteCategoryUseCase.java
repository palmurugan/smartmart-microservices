package com.smart.mart.catalog.application.port.input;

import java.util.UUID;

public interface DeleteCategoryUseCase {
    void delete(UUID id);
}

package com.smart.mart.catalog.infrastructure.config;

import com.smart.mart.catalog.domain.service.CategoryHierarchyValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CategoryHierarchyValidator categoryHierarchyValidator() {
        return new CategoryHierarchyValidator();
    }
}

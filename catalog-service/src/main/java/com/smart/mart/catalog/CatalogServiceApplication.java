package com.smart.mart.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatalogServiceApplication.class, args);
    }
}

// This annotation enables the service to be scanned when running in monolith mode
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(
    name = "service.catalog.enabled",
    havingValue = "true", 
    matchIfMissing = true
)
class CatalogServiceConfiguration {}
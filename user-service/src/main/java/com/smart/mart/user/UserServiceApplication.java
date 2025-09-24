package com.smart.mart.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}

// This annotation enables the service to be scanned when running in monolith mode
@org.springframework.boot.autoconfigure.condition.ConditionalOnProperty(
    name = "service.user.enabled", 
    havingValue = "true", 
    matchIfMissing = true
)
class UserServiceConfiguration {}
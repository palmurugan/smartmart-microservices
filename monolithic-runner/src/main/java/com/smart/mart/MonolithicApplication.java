package com.smart.mart;

import java.util.Map;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = {"com.erp.lite"})
@EnableJpaRepositories(basePackages = {"com.erp.lite"})
public class MonolithicApplication {

  public static void main(String[] args) {
    System.out.println("🚀 Starting Monolith Mode - All services in single JVM");
    SpringApplication.run(MonolithicApplication.class, args);
  }
}

@RestController
class MonolithHealthController {

  @GetMapping("/monolith/health")
  public Map<String, Object> health() {
    return Map.of(
        "status", "UP",
        "mode", "monolith",
        "services", Map.of(
            "user-service", "enabled",
            "auth-service", "enabled",
            "notification-service", "enabled"
        )
    );
  }
}

@Configuration
class MonolithConfiguration {
  // Any monolith-specific configuration
}
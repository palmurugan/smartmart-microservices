package com.smart.mart.user.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  @GetMapping
  public ResponseEntity<User> getAllUsers() {
    return ResponseEntity.ok(new User("Test User", "Active"));
  }

}

record User(String name, String status) {}
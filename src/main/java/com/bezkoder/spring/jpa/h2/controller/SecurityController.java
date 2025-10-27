package com.bezkoder.spring.jpa.h2.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/security")
public class SecurityController {

  @GetMapping("/public")
  public ResponseEntity<Map<String, String>> publicEndpoint() {
    return ResponseEntity.ok(Map.of("message", "Public security endpoint reached"));
  }

  @GetMapping("/user")
  public ResponseEntity<Map<String, String>> userEndpoint(Authentication authentication) {
    Map<String, String> response = new HashMap<>();
    response.put("message", "User endpoint reached");
    response.put("username", authentication.getName());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/admin")
  public ResponseEntity<Map<String, String>> adminEndpoint(Authentication authentication) {
    Map<String, String> response = new HashMap<>();
    response.put("message", "Admin endpoint reached");
    response.put("username", authentication.getName());
    return ResponseEntity.ok(response);
  }

  @GetMapping("/me")
  public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
    Map<String, Object> response = new HashMap<>();
    response.put("username", authentication.getName());
    response.put("authorities", authentication.getAuthorities()
      .stream()
      .map(GrantedAuthority::getAuthority)
      .collect(Collectors.toList()));
    return ResponseEntity.ok(response);
  }
}

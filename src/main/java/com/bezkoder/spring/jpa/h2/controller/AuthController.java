package com.bezkoder.spring.jpa.h2.controller;

import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.jpa.h2.model.User;
import com.bezkoder.spring.jpa.h2.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserRepository userRepository;

  public AuthController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
    return userRepository.findByUsernameIgnoreCase(request.username())
        .filter(user -> user.passwordMatches(request.password()))
        .map(this::buildSuccessResponse)
        .orElseGet(this::buildUnauthorizedResponse);
  }

  private ResponseEntity<LoginResponse> buildSuccessResponse(User user) {
    LoginResponse response = new LoginResponse("Login successful", user.getId(), user.getUsername());
    return ResponseEntity.ok(response);
  }

  private ResponseEntity<Map<String, String>> buildUnauthorizedResponse() {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(Map.of("message", "Invalid username or password"));
  }

  public static record LoginRequest(
      @NotBlank(message = "Username is required") String username,
      @NotBlank(message = "Password is required") String password) {
  }

  public static record LoginResponse(String message, Long userId, String username) {
  }
}

package com.bezkoder.spring.jpa.h2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.jpa.h2.model.User;
import com.bezkoder.spring.jpa.h2.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class AuthController {

  private final UserRepository userRepository;

  public AuthController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    if (request == null || !StringUtils.hasText(request.getUsername()) || !StringUtils.hasText(request.getPassword())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new LoginResponse("Username and password are required"));
    }

    return userRepository.findByUsernameIgnoreCase(request.getUsername())
        .map(user -> validatePassword(user, request.getPassword()))
        .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new LoginResponse("Invalid username or password")));
  }

  private ResponseEntity<LoginResponse> validatePassword(User user, String rawPassword) {
    if (user.getPassword().equals(rawPassword)) {
      return ResponseEntity.ok(new LoginResponse("Login successful"));
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
        .body(new LoginResponse("Invalid username or password"));
  }

  public static class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  }

  public static class LoginResponse {
    private final String message;

    public LoginResponse(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }
}

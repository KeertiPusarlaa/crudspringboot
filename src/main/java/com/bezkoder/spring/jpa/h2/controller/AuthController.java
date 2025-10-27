package com.bezkoder.spring.jpa.h2.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    if (request.username == null || request.username.isBlank() || request.password == null || request.password.isBlank()) {
      return new ResponseEntity<>(new LoginResponse("Username and password must be provided"), HttpStatus.BAD_REQUEST);
    }

    Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(request.username);
    if (userOptional.isEmpty()) {
      return new ResponseEntity<>(new LoginResponse("Invalid username or password"), HttpStatus.UNAUTHORIZED);
    }

    User user = userOptional.get();
    if (!passwordEncoder.matches(request.password, user.getPasswordHash())) {
      return new ResponseEntity<>(new LoginResponse("Invalid username or password"), HttpStatus.UNAUTHORIZED);
    }

    return new ResponseEntity<>(new LoginResponse("Login successful"), HttpStatus.OK);
  }

  public static class LoginRequest {
    public String username;
    public String password;

    public String getUsername() {
      return username;
    }

    public String getPassword() {
      return password;
    }
  }

  public static class LoginResponse {
    public final String message;

    public LoginResponse(String message) {
      this.message = message;
    }

    public String getMessage() {
      return message;
    }
  }
}


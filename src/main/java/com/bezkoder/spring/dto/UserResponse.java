package com.bezkoder.spring.dto;

import com.bezkoder.spring.model.Role;

public record UserResponse(Long id, String name, String email, Role role) {
}

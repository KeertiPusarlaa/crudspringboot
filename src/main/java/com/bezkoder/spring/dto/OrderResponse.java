package com.bezkoder.spring.dto;

import java.time.Instant;

public record OrderResponse(Long id, Long userId, Long productId, int quantity, Instant createdAt) {
}

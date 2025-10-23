package com.bezkoder.spring.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(
		@NotNull(message = "userId is required")
		Long userId,
		@NotNull(message = "productId is required")
		Long productId,
		@Positive(message = "quantity must be positive")
		int quantity) {
}

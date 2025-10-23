package com.bezkoder.spring.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.dto.PagedResponse;
import com.bezkoder.spring.dto.ProductResponse;
import com.bezkoder.spring.service.ProductService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public PagedResponse<ProductResponse> listProducts(
			@RequestParam(defaultValue = "0") @PositiveOrZero(message = "page must be greater than or equal to 0") int page,
			@RequestParam(defaultValue = "10") @Min(value = 1, message = "size must be greater than or equal to 1") int size) {
		return productService.getProducts(page, size);
	}
}

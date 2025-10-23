package com.bezkoder.spring.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.dto.PagedResponse;
import com.bezkoder.spring.dto.ProductResponse;
import com.bezkoder.spring.model.Product;
import com.bezkoder.spring.repository.ProductRepository;

@Service
public class ProductService {

	private static final int MAX_PAGE_SIZE = 50;

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Transactional(readOnly = true)
	public PagedResponse<ProductResponse> getProducts(int page, int size) {
		int resolvedSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
		int resolvedPage = Math.max(page, 0);

		Pageable pageable = PageRequest.of(resolvedPage, resolvedSize, Sort.by("id").ascending());
		Page<Product> productPage = productRepository.findAll(pageable);

		List<ProductResponse> content = productPage.getContent()
				.stream()
				.map(product -> new ProductResponse(
						product.getId(),
						product.getName(),
						product.getDescription(),
						product.getPrice()))
				.toList();

		return new PagedResponse<>(
				content,
				productPage.getNumber(),
				productPage.getSize(),
				productPage.getTotalElements(),
				productPage.getTotalPages());
	}
}

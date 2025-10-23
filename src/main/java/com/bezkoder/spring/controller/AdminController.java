package com.bezkoder.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.dto.AdminStatsResponse;
import com.bezkoder.spring.repository.AppUserRepository;
import com.bezkoder.spring.repository.ProductRepository;
import com.bezkoder.spring.repository.PurchaseOrderRepository;

@RestController
@RequestMapping("/admin")
public class AdminController {

	private final AppUserRepository userRepository;
	private final ProductRepository productRepository;
	private final PurchaseOrderRepository orderRepository;

	public AdminController(AppUserRepository userRepository,
			ProductRepository productRepository,
			PurchaseOrderRepository orderRepository) {
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.orderRepository = orderRepository;
	}

	@GetMapping("/stats")
	public AdminStatsResponse getStats() {
		return new AdminStatsResponse(
				userRepository.count(),
				productRepository.count(),
				orderRepository.count());
	}
}

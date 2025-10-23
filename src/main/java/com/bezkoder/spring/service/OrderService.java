package com.bezkoder.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.dto.OrderRequest;
import com.bezkoder.spring.dto.OrderResponse;
import com.bezkoder.spring.exception.ResourceNotFoundException;
import com.bezkoder.spring.model.AppUser;
import com.bezkoder.spring.model.Product;
import com.bezkoder.spring.model.PurchaseOrder;
import com.bezkoder.spring.repository.AppUserRepository;
import com.bezkoder.spring.repository.ProductRepository;
import com.bezkoder.spring.repository.PurchaseOrderRepository;

@Service
public class OrderService {

	private final PurchaseOrderRepository orderRepository;
	private final AppUserRepository userRepository;
	private final ProductRepository productRepository;

	public OrderService(PurchaseOrderRepository orderRepository,
			AppUserRepository userRepository,
			ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public OrderResponse createOrder(OrderRequest request) {
		AppUser user = userRepository.findById(request.userId())
				.orElseThrow(() -> new ResourceNotFoundException("User", request.userId()));
		Product product = productRepository.findById(request.productId())
				.orElseThrow(() -> new ResourceNotFoundException("Product", request.productId()));

		PurchaseOrder purchaseOrder = new PurchaseOrder(user, product, request.quantity());
		PurchaseOrder saved = orderRepository.save(purchaseOrder);

		return new OrderResponse(
				saved.getId(),
				saved.getUser().getId(),
				saved.getProduct().getId(),
				saved.getQuantity(),
				saved.getCreatedAt());
	}
}

package com.bezkoder.spring.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bezkoder.spring.dto.OrderRequest;
import com.bezkoder.spring.model.AppUser;
import com.bezkoder.spring.model.Product;
import com.bezkoder.spring.model.Role;
import com.bezkoder.spring.repository.AppUserRepository;
import com.bezkoder.spring.repository.ProductRepository;
import com.bezkoder.spring.repository.PurchaseOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private AppUserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PurchaseOrderRepository orderRepository;

	private AppUser standardUser;
	private AppUser adminUser;
	private Product product;

	@BeforeEach
	void setUp() {
		orderRepository.deleteAll();
		productRepository.deleteAll();
		userRepository.deleteAll();

		standardUser = userRepository.save(new AppUser("Alice", "alice@example.com", Role.USER));
		adminUser = userRepository.save(new AppUser("Bob", "admin@example.com", Role.ADMIN));
		product = productRepository.save(new Product("Widget", "Useful widget", new BigDecimal("9.99")));
	}

	@Test
	@DisplayName("GET /users/{id} returns user details")
	void getUserReturnsDetails() throws Exception {
		mockMvc.perform(get("/users/{id}", standardUser.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(standardUser.getId()))
				.andExpect(jsonPath("$.name").value("Alice"))
				.andExpect(jsonPath("$.email").value("alice@example.com"))
				.andExpect(jsonPath("$.role").value("USER"));
	}

	@Test
	@DisplayName("GET /users/{id} responds with 404 when user not found")
	void getUserNotFound() throws Exception {
		mockMvc.perform(get("/users/{id}", 9999))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.status").value(404))
				.andExpect(jsonPath("$.error").value("Not Found"))
				.andExpect(jsonPath("$.message").value("User not found with id 9999"));
	}

	@Test
	@DisplayName("POST /orders validates request body and returns field errors")
	void createOrderValidationError() throws Exception {
		OrderRequest request = new OrderRequest(null, product.getId(), 0);

		mockMvc.perform(post("/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.fieldErrors").isArray())
				.andExpect(jsonPath("$.fieldErrors.length()").value(2));
	}

	@Test
	@DisplayName("POST /orders creates an order when payload is valid")
	void createOrderSuccess() throws Exception {
		OrderRequest request = new OrderRequest(standardUser.getId(), product.getId(), 3);

		mockMvc.perform(post("/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.userId").value(standardUser.getId()))
				.andExpect(jsonPath("$.productId").value(product.getId()))
				.andExpect(jsonPath("$.quantity").value(3));
	}

	@Test
	@DisplayName("GET /products returns paginated results")
	void listProductsReturnsPagedResponse() throws Exception {
		mockMvc.perform(get("/products")
				.param("page", "0")
				.param("size", "1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.page").value(0))
				.andExpect(jsonPath("$.size").value(1))
				.andExpect(jsonPath("$.content.length()").value(1));
	}

	@Test
	@DisplayName("GET /products enforces pagination parameter validation")
	void listProductsValidationError() throws Exception {
		mockMvc.perform(get("/products")
				.param("page", "-1")
				.param("size", "0"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.fieldErrors").isArray());
	}

	@Test
	@DisplayName("GET /admin/stats requires admin role")
	void adminStatsRequiresAdminRole() throws Exception {
		mockMvc.perform(get("/admin/stats"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("GET /admin/stats returns aggregated stats for admins")
	void adminStatsReturnsData() throws Exception {
		mockMvc.perform(get("/admin/stats")
				.with(httpBasic("admin", "password")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userCount").value(2))
				.andExpect(jsonPath("$.productCount").value(1))
				.andExpect(jsonPath("$.orderCount").value(0));
	}
}

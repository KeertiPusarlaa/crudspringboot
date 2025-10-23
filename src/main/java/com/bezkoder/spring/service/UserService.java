package com.bezkoder.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.dto.UserResponse;
import com.bezkoder.spring.exception.ResourceNotFoundException;
import com.bezkoder.spring.model.AppUser;
import com.bezkoder.spring.repository.AppUserRepository;

@Service
public class UserService {

	private final AppUserRepository userRepository;

	public UserService(AppUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public UserResponse getUserById(Long id) {
		AppUser user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", id));
		return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getRole());
	}
}

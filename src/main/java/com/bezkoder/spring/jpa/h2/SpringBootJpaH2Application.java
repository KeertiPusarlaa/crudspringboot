package com.bezkoder.spring.jpa.h2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bezkoder.spring.jpa.h2.model.User;
import com.bezkoder.spring.jpa.h2.repository.UserRepository;

@SpringBootApplication
public class SpringBootJpaH2Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJpaH2Application.class, args);
	}

	@Bean
	CommandLineRunner ensureDefaultUser(UserRepository userRepository) {
		return args -> userRepository.findByUsernameIgnoreCase("admin")
				.orElseGet(() -> userRepository.save(new User("admin", "password")));
	}

}

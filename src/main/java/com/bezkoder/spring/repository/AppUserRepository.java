package com.bezkoder.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
}

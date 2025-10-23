package com.bezkoder.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.model.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}

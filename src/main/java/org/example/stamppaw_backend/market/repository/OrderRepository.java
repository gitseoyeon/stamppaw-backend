package org.example.stamppaw_backend.market.repository;

import org.example.stamppaw_backend.market.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository  extends JpaRepository<Order, Long> {
}

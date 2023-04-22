package com.kiet.clothesshop.repository;

import com.kiet.clothesshop.model.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

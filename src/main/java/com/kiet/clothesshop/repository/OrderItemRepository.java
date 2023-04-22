package com.kiet.clothesshop.repository;

import com.kiet.clothesshop.model.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}

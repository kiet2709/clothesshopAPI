package com.kiet.clothesshop.repository;

import com.kiet.clothesshop.model.order.OrderTrack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderTrackRepository extends JpaRepository<OrderTrack, Long>{

	Optional<OrderTrack> findByStatus(String delivering);

}

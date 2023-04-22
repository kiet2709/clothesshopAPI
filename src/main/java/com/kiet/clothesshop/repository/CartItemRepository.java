package com.kiet.clothesshop.repository;

import com.kiet.clothesshop.model.cart.Cart;
import com.kiet.clothesshop.model.cart.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {


	Optional<CartItem> findByCartAndId(Cart cart, Long id);


	List<CartItem> findByIdInAndCart(List<Long> cartItemId, Cart cart);

}

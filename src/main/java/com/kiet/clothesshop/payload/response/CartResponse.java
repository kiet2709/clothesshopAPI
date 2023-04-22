package com.kiet.clothesshop.payload.response;

import com.kiet.clothesshop.model.cart.CartItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
	
	private Long id;
	
	private List<CartItem> cartItems;
	
	private Long userId;
}

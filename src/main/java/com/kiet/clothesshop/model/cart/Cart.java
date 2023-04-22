package com.kiet.clothesshop.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kiet.clothesshop.model.user.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "carts")
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	@OneToMany(mappedBy = "cart")
	private List<CartItem> cartItems;
	
	@OneToOne(mappedBy = "cart", fetch = FetchType.LAZY)
	@JsonIgnore
	private User user;
	
	public void addCartItem(CartItem cartItem) {
		cartItems.add(cartItem);
	}
	
}

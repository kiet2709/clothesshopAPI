package com.kiet.clothesshop.payload.response;


import com.kiet.clothesshop.model.cart.Cart;
import com.kiet.clothesshop.model.order.Order;
import com.kiet.clothesshop.model.user.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	private Long id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private Boolean enabled;
	private Date birthday;
	private String address;
	private Cart cart;
	private List<Order> orders;
	private List<Role> roles;
	private String image;


	
}

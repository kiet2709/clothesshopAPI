package com.kiet.clothesshop.payload.response;

import com.kiet.clothesshop.model.order.OrderItem;
import com.kiet.clothesshop.model.order.OrderTrack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

	private Long id;

	private Date orderDate;

	private String deliverMethod;

	private Double deliverCost;

	private List<OrderItem> orderItems;

	private OrderTrack orderTrack;

	private Long userId;
	
	private Double totalPrice;
	
	private Double totalProductPrice;
}

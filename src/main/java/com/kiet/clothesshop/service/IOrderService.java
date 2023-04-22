package com.kiet.clothesshop.service;

import com.kiet.clothesshop.payload.request.OrderRequest;
import com.kiet.clothesshop.payload.response.OrderResponse;

import java.util.List;


public interface IOrderService {

	OrderResponse getOrderById(Long orderId);

	List<OrderResponse> getAllOrders();

	List<OrderResponse> getCurrentOrderByUserId(Long id);

	OrderResponse submitOrder(Long id, OrderRequest orderRequest);

}

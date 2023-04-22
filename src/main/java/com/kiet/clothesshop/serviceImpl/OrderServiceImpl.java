package com.kiet.clothesshop.serviceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kiet.clothesshop.exception.ResourceNotFoundException;
import com.kiet.clothesshop.model.Inventory;
import com.kiet.clothesshop.model.cart.CartItem;
import com.kiet.clothesshop.model.order.Order;
import com.kiet.clothesshop.model.order.OrderItem;
import com.kiet.clothesshop.model.order.OrderTrack;
import com.kiet.clothesshop.model.user.User;
import com.kiet.clothesshop.payload.request.OrderRequest;
import com.kiet.clothesshop.payload.response.OrderResponse;
import com.kiet.clothesshop.repository.*;
import com.kiet.clothesshop.service.IOrderService;
import com.kiet.clothesshop.untils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements IOrderService {

	@Autowired
	OrderRepository orderRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	OrderItemRepository orderItemRepository;

	@Autowired
	InventoryRepository inventoryRepository;

	@Autowired
	OrderTrackRepository orderTrackRepository;

	@Override
	public OrderResponse getOrderById(Long orderId) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ORDER_NOT_FOUND+orderId));
		return new OrderResponse(
				order.getId(),
				order.getOrderDate(),
				order.getDeliverMethod(),
				order.getDeliverCost(),
				order.getOrderItems(),
				order.getOrderTrack(),
				order.getUser().getId(),
				order.getTotalPrice(),
				order.getTotalProductPrice()
		);
	}

	@Override
	public List<OrderResponse> getAllOrders() {
		List<Order> orders = orderRepository.findAll();
		List<OrderResponse> orderResponses = new ArrayList<>();
		for (Order order: orders) {
			OrderResponse orderResponse = new OrderResponse(
					order.getId(),
					order.getOrderDate(),
					order.getDeliverMethod(),
					order.getDeliverCost(),
					order.getOrderItems(),
					order.getOrderTrack(),
					order.getUser().getId(),
					order.getTotalPrice(),
					order.getTotalProductPrice()
			);
			orderResponses.add(orderResponse);
		}
		return orderResponses;
	}

	@Override
	public List<OrderResponse> getCurrentOrderByUserId(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND+id));

		List<OrderResponse> orderResponses = new ArrayList<>();
		for (Order order: user.getOrders()) {
			OrderResponse orderResponse = new OrderResponse(
					order.getId(),
					order.getOrderDate(),
					order.getDeliverMethod(),
					order.getDeliverCost(),
					order.getOrderItems(),
					order.getOrderTrack(),
					order.getUser().getId(),
					order.getTotalPrice(),
					order.getTotalProductPrice()
			);
			orderResponses.add(orderResponse);
		}
		return orderResponses;
	}

	@Override
	public OrderResponse submitOrder(Long id, OrderRequest orderRequest) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND+id));

		List<CartItem> cartItems = cartItemRepository.findByIdInAndCart(orderRequest.getCartItemId(),user.getCart());

		Order order = processOrder(user,cartItems,orderRequest);

		OrderResponse orderResponse = new OrderResponse();
		orderResponse.setId(order.getId());
		orderResponse.setDeliverMethod(order.getDeliverMethod());
		orderResponse.setDeliverCost(order.getDeliverCost());
		orderResponse.setOrderDate(order.getOrderDate());
		orderResponse.setOrderTrack(order.getOrderTrack());
		orderResponse.setOrderItems(order.getOrderItems());
		orderResponse.setTotalPrice(order.getTotalPrice());
		orderResponse.setTotalProductPrice(order.getTotalProductPrice());
		orderResponse.setUserId(id);

		return orderResponse;
	}

	public Order processOrder(User user,List<CartItem> cartItems, OrderRequest orderRequest) {

		OrderTrack orderTrack = orderTrackRepository.findByStatus(AppConstant.PREPARING)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.ORDER_TRACK_NOT_FOUND+AppConstant.PREPARING));

		Order order = new Order();
		order.setUser(user);
		order.setDeliverMethod(orderRequest.getDeliverMethod());

		if(orderRequest.getDeliverMethod().equals(AppConstant.VERY_FAST)) {
			order.setDeliverCost(AppConstant.VERY_FAST_COST);
		}else if(orderRequest.getDeliverMethod().equals(AppConstant.FAST)) {
			order.setDeliverCost(AppConstant.FAST_COST);
		}else {
			order.setDeliverCost(AppConstant.STANDARD_COST);
		}

		order.setOrderDate(new Date());
		order.setOrderTrack(orderTrack);

		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setCloth(cartItem.getCloth());

			OrderItem savedOItem =  orderItemRepository.save(orderItem);
			order.addOrderItems(savedOItem);

			Inventory inventory = inventoryRepository.findByClothAndSize(cartItem.getCloth(),cartItem.getChoice_size())
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.INVENTORY_NOT_FOUND));
			inventory.setQuantity(inventory.getQuantity()-orderItem.getQuantity());
			inventoryRepository.save(inventory);
			cartItemRepository.delete(cartItem);

		}

		return orderRepository.save(order);
	}

}

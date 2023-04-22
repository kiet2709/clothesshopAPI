package com.kiet.clothesshop.controller;

import com.kiet.clothesshop.payload.request.OrderRequest;
import com.kiet.clothesshop.payload.response.OrderResponse;
import com.kiet.clothesshop.security.CurrentUser;
import com.kiet.clothesshop.security.UserPrincipal;
import com.kiet.clothesshop.service.IOrderService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

	@Autowired
	IOrderService orderService;

	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<OrderResponse>> getAllOrders(){
		List<OrderResponse> orders = orderService.getAllOrders();
		return new ResponseEntity<>(orders,HttpStatus.OK);
	}

	@GetMapping("/{order_id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<OrderResponse> getOrderById(
			@PathVariable("order_id") Long orderId){
		OrderResponse order = orderService.getOrderById(orderId);
		return new ResponseEntity<>(order,HttpStatus.OK);
	}

	@GetMapping("/current")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<OrderResponse>> getCurrentOrderByUserId(@CurrentUser UserPrincipal userPrincipal){
		List<OrderResponse> orders = orderService.getCurrentOrderByUserId(userPrincipal.getId());
		return new ResponseEntity<>(orders,HttpStatus.OK);
	}

	@PostMapping("/current/submit")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<OrderResponse> submitOrder(
			@CurrentUser UserPrincipal userPrincipal,
			@RequestBody OrderRequest orderRequest){
		OrderResponse order = orderService.submitOrder(userPrincipal.getId(),orderRequest);
		return new ResponseEntity<>(order,HttpStatus.OK);
	}
}

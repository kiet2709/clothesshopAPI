package com.kiet.clothesshop.controller;

import com.kiet.clothesshop.payload.request.CartItemRequest;
import com.kiet.clothesshop.payload.response.ApiResponse;
import com.kiet.clothesshop.payload.response.CartResponse;
import com.kiet.clothesshop.security.CurrentUser;
import com.kiet.clothesshop.security.UserPrincipal;
import com.kiet.clothesshop.service.ICartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/carts")
public class CartController {

	@Autowired
	ICartService cartService;
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<CartResponse>> getAllCart(){
		List<CartResponse> carts = cartService.getAllCart();
		return new ResponseEntity<>(carts,HttpStatus.OK);
	}
	
	@GetMapping("/{cart_id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CartResponse> getCartById(
			@PathVariable("cart_id") Long cartId){
		CartResponse cart = cartService.getCartById(cartId);
		return new ResponseEntity<>(cart,HttpStatus.OK);
	}
	
	@GetMapping("/current/items")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<CartResponse> getCurrentCartByUserId(
			@CurrentUser UserPrincipal userPrincipal){
		System.out.println(userPrincipal.getFirstName()+"===============================");
		CartResponse cart = cartService.getCurrentCartByUserId(userPrincipal.getId());
		return new ResponseEntity<>(cart,HttpStatus.OK);
	}
	
	@PostMapping("/current/items/add")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<CartResponse> addCartItemByUserId(
			@CurrentUser UserPrincipal userPrincipal,
			@RequestBody CartItemRequest itemRequest){
		CartResponse cart = cartService.addCartItemByUserId(userPrincipal.getId(),itemRequest);
		return new ResponseEntity<>(cart,HttpStatus.OK);
	}
	
	@DeleteMapping("/current/items/{cart_item_id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse> deleteCartItemById(
			@CurrentUser UserPrincipal userPrincipal,
			@PathVariable("cart_item_id") Long cartItemId){
		ApiResponse response = cartService.deleteCartItemById(userPrincipal.getId(),cartItemId);
		return new ResponseEntity<>(response,HttpStatus.OK);
	}
	
	@PatchMapping("/current/items/{cart_item_id}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<CartResponse> updateCartItemById(
			@CurrentUser UserPrincipal userPrincipal,
			@PathVariable("cart_item_id") Long cartItemId,
			@RequestBody CartItemRequest itemRequest){
		CartResponse cart = cartService.updateCartItemById(userPrincipal.getId(),cartItemId,itemRequest);
		return new ResponseEntity<>(cart,HttpStatus.OK);
	}

}

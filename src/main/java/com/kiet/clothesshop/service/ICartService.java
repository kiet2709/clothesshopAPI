package com.kiet.clothesshop.service;

import com.kiet.clothesshop.payload.request.CartItemRequest;
import com.kiet.clothesshop.payload.response.ApiResponse;
import com.kiet.clothesshop.payload.response.CartResponse;

import java.util.List;


public interface ICartService {

	CartResponse getCartById(Long cartId);

	List<CartResponse> getAllCart();

	CartResponse getCurrentCartByUserId(Long userId);

	CartResponse addCartItemByUserId(Long id, CartItemRequest itemRequest);

	ApiResponse deleteCartItemById(Long id, Long cartItemId);

	CartResponse updateCartItemById(Long id, Long cartItemId, CartItemRequest itemRequest);

}

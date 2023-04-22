package com.kiet.clothesshop.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.kiet.clothesshop.exception.ResourceNotFoundException;
import com.kiet.clothesshop.model.Cloth;
import com.kiet.clothesshop.model.Size;
import com.kiet.clothesshop.model.cart.Cart;
import com.kiet.clothesshop.model.cart.CartItem;
import com.kiet.clothesshop.model.user.User;
import com.kiet.clothesshop.payload.request.CartItemRequest;
import com.kiet.clothesshop.payload.response.ApiResponse;
import com.kiet.clothesshop.payload.response.CartResponse;
import com.kiet.clothesshop.repository.*;
import com.kiet.clothesshop.service.ICartService;
import com.kiet.clothesshop.untils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;



import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CartServiceImpl implements ICartService {

	@Autowired
	CartRepository cartRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ClothRepository clothRepository;
	
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	SizeRepository sizeRepository;

	
	@Override
	public CartResponse getCartById(Long cartId) {

		
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CART_NOT_FOUND+cartId));

		
		return new CartResponse(
			cart.getId(), 
			cart.getCartItems(), 
			cart.getUser().getId()
		);
	}

	@Override
	public List<CartResponse> getAllCart() {
		List<Cart> carts = cartRepository.findAll();
		List<CartResponse> cartResponses = new ArrayList<>();
		for (Cart cart : carts) {
			CartResponse cartResponse = new CartResponse(
				cart.getId(),
				cart.getCartItems(),
				cart.getUser().getId()
			);
			cartResponses.add(cartResponse);
		}
		return cartResponses;
	}

	@Override
	public CartResponse getCurrentCartByUserId(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND+userId));
		
		return new CartResponse(
			user.getCart().getId(),
			user.getCart().getCartItems(),
			user.getId()
		);
	}

	@Override
	public CartResponse addCartItemByUserId(Long id, CartItemRequest itemRequest) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND+id));
		
		Cart cart = user.getCart();
		
		// get cloth
		Long clothId = itemRequest.getClothId();
		Cloth cloth = clothRepository.findById(clothId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.CLOTH_NOT_FOUND+clothId));
		
		CartItem cartItemOld = checkClothExist(cart, cloth);
		
		if(Objects.nonNull(cartItemOld)) {
			
			// increase quantity
			cartItemOld.setQuantity(cartItemOld.getQuantity() + itemRequest.getQuantity());
			cartItemRepository.save(cartItemOld);
		}else {
			
			Size size = sizeRepository.findById(itemRequest.getChoice_sizeId())
					.orElseThrow(() -> new ResourceNotFoundException(AppConstant.SIZE_NOT_FOUND+itemRequest.getChoice_sizeId()));
			
			// create cartItem
			CartItem cartItem = new CartItem();
			cartItem.setQuantity(itemRequest.getQuantity());
			cartItem.setChoice_size(size);
			cartItem.setCloth(cloth);
			cartItem.setCart(cart);
			cartItemRepository.save(cartItem);
			
			// add again so that cartResponse will be updated
			cart.addCartItem(cartItem);
			cartRepository.save(cart);
		}
		
		
		return new CartResponse(
			cart.getId(), 
			cart.getCartItems(), 
			user.getId()
		);
	}
	
	public CartItem checkClothExist(Cart cart, Cloth cloth) {
		
		for (CartItem cartItem : cart.getCartItems()) {
			if(cartItem.getCloth().getId().equals(cloth.getId())) {
				return cartItem;
			}
		}
		
		return null;
	}

	@Override
	public ApiResponse deleteCartItemById(Long id, Long cartItemId) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND+id));
		
		Boolean flag = false;
		
		for (CartItem item : user.getCart().getCartItems()) {
			if(item.getId().equals(cartItemId)) {
				cartItemRepository.delete(item);
				flag=true;
			}
		}
		if(flag==true) {
			return new ApiResponse(Boolean.TRUE,AppConstant.DELETE_SUCCESS,HttpStatus.OK);
		}else {
			return new ApiResponse(Boolean.FALSE,AppConstant.DELETE_FAILURE,HttpStatus.OK);
		}
		
	}

	@Override
	public CartResponse updateCartItemById(Long id, Long cartItemId, CartItemRequest itemRequest) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND+id));
		
		Size size = sizeRepository.findById(itemRequest.getChoice_sizeId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.SIZE_NOT_FOUND+itemRequest.getChoice_sizeId()));
		
		Boolean flag = false;
		
		for (CartItem item : user.getCart().getCartItems()) {
			if(item.getId().equals(cartItemId)) {
				item.setQuantity(itemRequest.getQuantity());
				item.setChoice_size(size);
				cartItemRepository.save(item);
				flag = true;
			}
		}
		
		if(flag.equals(false)) {
			throw new ResourceNotFoundException(AppConstant.CART_ITEM_NOT_FOUND_USER+id);
		}

		return new CartResponse(
			user.getCart().getId(),
			user.getCart().getCartItems(),
			user.getId()
		);
	}

}

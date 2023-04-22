package com.kiet.clothesshop.service;

import java.io.IOException;
import java.util.List;

import com.kiet.clothesshop.model.Image;
import com.kiet.clothesshop.payload.request.UserProfileRequest;
import com.kiet.clothesshop.payload.request.UserRequest;
import com.kiet.clothesshop.payload.response.CartResponse;
import com.kiet.clothesshop.payload.response.OrderResponse;
import com.kiet.clothesshop.payload.response.UserProfileResponse;
import com.kiet.clothesshop.payload.response.UserResponse;
import com.kiet.clothesshop.security.UserPrincipal;
import org.springframework.web.multipart.MultipartFile;


public interface IUserService {

	List<UserResponse> getAllUsers();

	UserResponse getUserById(Long userId);

	CartResponse getCartByUserId(Long userId);

	List<OrderResponse> getOrdersByUserId(Long userId);

	UserProfileResponse createUser(UserRequest userRequest);

	UserProfileResponse getCurrentUser(UserPrincipal userPrincipal);

	Boolean checkUsernameUnique(String username);

	Boolean checkEmailUnique(String email);

	CartResponse getCartByCurrentUser(UserPrincipal userPrincipal);

	List<OrderResponse> getOrdersByCurrentUser(UserPrincipal userPrincipal);

	UserProfileResponse updateCurrentProfile(UserPrincipal userPrincipal, UserProfileRequest userProfileRequest);

	UserProfileResponse uploadImage(MultipartFile file, UserPrincipal userPrincipal) throws IOException;

	Image getImagesById(UserPrincipal userPrincipal);


}

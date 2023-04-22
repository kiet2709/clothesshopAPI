package com.kiet.clothesshop.serviceImpl;

import java.io.IOException;
import java.sql.Date;
import java.time.ZoneId;
import java.util.*;

import com.kiet.clothesshop.exception.ResourceExistException;
import com.kiet.clothesshop.exception.ResourceNotFoundException;
import com.kiet.clothesshop.exception.UserNotFoundException;
import com.kiet.clothesshop.model.Image;
import com.kiet.clothesshop.model.cart.Cart;
import com.kiet.clothesshop.model.order.Order;
import com.kiet.clothesshop.model.user.Role;
import com.kiet.clothesshop.model.user.RoleName;
import com.kiet.clothesshop.model.user.User;
import com.kiet.clothesshop.payload.request.UserProfileRequest;
import com.kiet.clothesshop.payload.request.UserRequest;
import com.kiet.clothesshop.payload.response.CartResponse;
import com.kiet.clothesshop.payload.response.OrderResponse;
import com.kiet.clothesshop.payload.response.UserProfileResponse;
import com.kiet.clothesshop.payload.response.UserResponse;
import com.kiet.clothesshop.repository.ImageRepository;
import com.kiet.clothesshop.repository.RoleRepository;
import com.kiet.clothesshop.repository.UserRepository;
import com.kiet.clothesshop.security.UserPrincipal;
import com.kiet.clothesshop.service.IUserService;
import com.kiet.clothesshop.untils.AppConstant;
import com.kiet.clothesshop.untils.FileUploadUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UserServiceImpl implements IUserService {


	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	ImageRepository imageRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	ModelMapper mapper;

	@Override
	public List<UserResponse> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserResponse> userResponses = new ArrayList<>();
		for (User user: users) {
			UserResponse userResponse = new UserResponse(
					user.getId(),
					user.getUsername(),
					user.getEmail(),
					user.getFirstName(),
					user.getLastName(),
					user.getPhoneNumber(),
					user.getEnabled(),
					Date.valueOf(user.getBirthday()),
					user.getAddress(),
					user.getCart(),
					user.getOrders(),
					user.getRoles(),
					user.getImage().toString()
			);
			userResponses.add(userResponse);
		}
		return userResponses;
	}

	@Override
	public UserResponse getUserById(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND+userId));
		return new UserResponse(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				user.getPhoneNumber(),
				user.getEnabled(),
				Date.valueOf(user.getBirthday()),
				user.getAddress(),
				user.getCart(),
				user.getOrders(),
				user.getRoles(),
				user.getImage().toString()
		);
	}

	@Override
	public CartResponse getCartByUserId(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND+userId));;
		return new CartResponse(
				user.getCart().getId(),
				user.getCart().getCartItems(),
				user.getId()
		);
	}

	@Override
	public List<OrderResponse> getOrdersByUserId(Long userId) {
		// skip id so that modelmapping not map cartId with userId
		mapper.typeMap(OrderResponse.class, Order.class)
				.addMappings(mapper -> mapper.skip(Order::setId));

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND+userId));
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
	public UserProfileResponse createUser(UserRequest userRequest) {
		String username = userRequest.getUsername();
		if(userRepository.existsByUsername(username)) {
			throw new ResourceExistException(AppConstant.USER_EXIST);
		}

		Role role = roleRepository.findByName(RoleName.USER);
		if(Objects.isNull(role)) {
			throw new ResourceNotFoundException(AppConstant.ROLE_NOT_FOUND + RoleName.USER);
		}

		User user = new User();
		//map user by userRequest
		user.setUsername(userRequest.getUsername());
		user.setEmail(userRequest.getEmail());
		user.setFirstName(userRequest.getFirstName());
		user.setLastName(userRequest.getLastName());
		user.setPhoneNumber(userRequest.getPhoneNumber());
		user.setBirthday(userRequest.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		user.setAddress(userRequest.getAddress());
		user.setPassword(userRequest.getPassword());
		
		user.setRoles(Arrays.asList(role));
		user.setPassword(encoder.encode(user.getPassword()));
		userRepository.save(user);

		return new UserProfileResponse(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				user.getPhoneNumber(),
				user.getImage(),
				user.getBirthday(),
				user.getAddress()
		);
	}

	@Override
	public UserProfileResponse getCurrentUser(UserPrincipal userPrincipal) {
		String username = userPrincipal.getUsername();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(
						AppConstant.USER_NOT_FOUND + username));


		UserProfileResponse userProfile = new UserProfileResponse();
		userProfile.setId(user.getId());
		userProfile.setUsername(username);
		userProfile.setFirstName(user.getFirstName());
		userProfile.setLastName(user.getLastName());
		userProfile.setEmail(user.getEmail());
		userProfile.setPhoneNumber(user.getPhoneNumber());
		userProfile.setImage(user.getImage());
		userProfile.setAddress(user.getAddress());
		userProfile.setBirthday(user.getBirthday());
		return userProfile;
	}

	@Override
	public Boolean checkUsernameUnique(String username) {
		return !userRepository.existsByUsername(username);
	}

	@Override
	public Boolean checkEmailUnique(String email) {
		return !userRepository.existsByEmail(email);
	}

	@Override
	public CartResponse getCartByCurrentUser(UserPrincipal userPrincipal) {
		String username = userPrincipal.getUsername();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(
						AppConstant.USER_NOT_FOUND + username));

		return new CartResponse(
				user.getCart().getId(),
				user.getCart().getCartItems(),
				user.getId()
		);
	}

	@Override
	public List<OrderResponse> getOrdersByCurrentUser(UserPrincipal userPrincipal) {
		String username = userPrincipal.getUsername();
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(
						AppConstant.USER_NOT_FOUND + username));

		List<Order> orders = user.getOrders();
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
	public UserProfileResponse updateCurrentProfile(UserPrincipal userPrincipal,
													UserProfileRequest userProfileRequest) {
		User user = userRepository.findById(userPrincipal.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND+userPrincipal.getId()));

		User savedUser = updateProfile(user, userProfileRequest);
		return new UserProfileResponse(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				user.getPhoneNumber(),
				user.getImage(),
				user.getBirthday(),
				user.getAddress()
		);
	}

	public User updateProfile(User user, UserProfileRequest userProfileRequest) {

		user.setFirstName(userProfileRequest.getFirstName());
		user.setLastName(userProfileRequest.getLastName());
		user.setAddress(userProfileRequest.getAddress());
		user.setBirthday(userProfileRequest.getBirthday());
		user.setPhoneNumber(userProfileRequest.getPhoneNumber());
		return userRepository.save(user) ;
	}

	@Override
	public UserProfileResponse uploadImage(MultipartFile file, UserPrincipal userPrincipal) throws IOException {
		Long userID = userPrincipal.getId();

		User user = userRepository.findById(userID)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND+userID));

		Image image;
		if(user.getImage()!=null) {
			image = user.getImage();
			uploadImage(file, userPrincipal, userID, user, image);
		}else {
			image = new Image();
			uploadImage(file, userPrincipal, userID, user, image);
		}

		return new UserProfileResponse(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getFirstName(),
				user.getLastName(),
				user.getPhoneNumber(),
				user.getImage(),
				user.getBirthday(),
				user.getAddress()
		);
	}

	private void uploadImage(MultipartFile file, UserPrincipal userPrincipal, Long userID, User user, Image image) throws IOException {
		if (!file.isEmpty()) {
			FileUploadUtils.saveUserImage(file, userID);
			image.setTitle(userPrincipal.getId() + ".png");
			image.setPath(AppConstant.UPLOAD_USER_DIRECTORY+"/"+ userID +".png");
			imageRepository.save(image);
			user.setImage(image);
			userRepository.save(user);
		}
	}

	@Override
	public Image getImagesById(UserPrincipal userPrincipal) {
		User user = userRepository.findById(userPrincipal.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.USER_ID_NOT_FOUND+userPrincipal.getId()));
		return user.getImage();
	}


}

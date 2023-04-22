package com.kiet.clothesshop.controller;

import com.kiet.clothesshop.payload.request.LoginRequest;
import com.kiet.clothesshop.payload.request.UserRequest;
import com.kiet.clothesshop.payload.response.JwtResponse;
import com.kiet.clothesshop.payload.response.UserProfileResponse;
import com.kiet.clothesshop.security.JwtTokenProvider;
import com.kiet.clothesshop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

	@Autowired
	IUserService userService;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	JwtTokenProvider tokenProvider;
	
	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> signin(
			@RequestBody LoginRequest loginRequest){
		
		UsernamePasswordAuthenticationToken authReq
		 = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
		Authentication auth = authManager.authenticate(authReq);
		String token = tokenProvider.generateToken(auth);
		SecurityContextHolder.getContext().setAuthentication(auth);
		return new ResponseEntity<>(new JwtResponse(token,"jwt"),HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserProfileResponse> register(
			@RequestBody UserRequest userRequest
			){
		UserProfileResponse userProfile = userService.createUser(userRequest);
		return new ResponseEntity<>(userProfile,HttpStatus.CREATED);
	}
}

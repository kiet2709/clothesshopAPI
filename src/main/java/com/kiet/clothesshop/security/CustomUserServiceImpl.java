package com.kiet.clothesshop.security;


import com.kiet.clothesshop.exception.UserNotFoundException;
import com.kiet.clothesshop.model.user.User;

import com.kiet.clothesshop.repository.UserRepository;
import com.kiet.clothesshop.service.ICustomUserService;
import com.kiet.clothesshop.untils.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserServiceImpl implements UserDetailsService, ICustomUserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UserNotFoundException(
						AppConstant.USER_NOT_FOUND + username));
		return UserPrincipal.create(user);

	}

	@Override
	public UserPrincipal loadUserByUserId(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(
						AppConstant.USER_NOT_FOUND + "id" + userId));
		return UserPrincipal.create(user);
	}



}

package com.kiet.clothesshop.service;


import com.kiet.clothesshop.security.UserPrincipal;

public interface ICustomUserService {
	public UserPrincipal loadUserByUserId(Long userId);
}

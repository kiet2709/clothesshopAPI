package com.kiet.clothesshop.payload.request;

import lombok.Data;

import java.util.Date;

@Data
public class UserRequest {
	
	private String username;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private Date birthday;
	private String address;
	private String image;
}

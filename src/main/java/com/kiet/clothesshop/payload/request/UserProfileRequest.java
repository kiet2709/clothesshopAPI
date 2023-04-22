package com.kiet.clothesshop.payload.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserProfileRequest {

	private String firstName;
	private String lastName;
	private String phoneNumber;
	private LocalDate birthday;
	private String address;
	private String image;
}

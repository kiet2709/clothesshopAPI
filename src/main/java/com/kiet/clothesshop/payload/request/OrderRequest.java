package com.kiet.clothesshop.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
	
	private String deliverMethod;
	
	private List<Long> cartItemId;
}

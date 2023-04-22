package com.kiet.clothesshop.payload.request;

import lombok.Data;

@Data
public class InventoryRequest {

	private Integer quantity;
	
	private Long sizeId;
	
	private Long clothId;
}

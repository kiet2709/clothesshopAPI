package com.kiet.clothesshop.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class ClothRequest {


	private String name;
	
	private Double price;
	
	private String description;
	
	private List<InventoryRequest> inventory;
	
	private Long brandId;
	
	private Long categoryId;
}

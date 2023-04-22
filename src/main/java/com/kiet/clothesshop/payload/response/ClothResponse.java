package com.kiet.clothesshop.payload.response;

import com.kiet.clothesshop.model.Brand;
import com.kiet.clothesshop.model.Category;
import com.kiet.clothesshop.model.Image;
import com.kiet.clothesshop.model.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClothResponse {

	private Long id;
	
	private String name;
	
	private Integer price;
	
	private String description;
	
	private List<Inventory> inventories;
	
	private Brand brand;
	
	private Category category;
	
	public void addInventory(Inventory inventory) {
		if(this.inventories == null) {
			this.inventories = new ArrayList<>();
		}
		inventories.add(inventory);
	}
	
	private Image image;
}

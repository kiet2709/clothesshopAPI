package com.kiet.clothesshop.model.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.kiet.clothesshop.model.Cloth;
import com.kiet.clothesshop.model.Size;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "cart_item")
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column(name = "quantity", nullable = false)
	private Integer quantity;
	
	@ManyToOne
	@JoinColumn(name = "cloth_id", referencedColumnName = "id")
	private Cloth cloth;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", referencedColumnName = "id")
	@JsonIgnore
	private Cart cart;

	@ManyToOne
	@JoinColumn(name="size_id",referencedColumnName = "id")
	private Size choice_size;
	
	public Double getTotalPrice() {
		return quantity * cloth.getPrice();
	}
}

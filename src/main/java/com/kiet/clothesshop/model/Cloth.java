package com.kiet.clothesshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.kiet.clothesshop.model.cart.CartItem;
import com.kiet.clothesshop.model.order.OrderItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clothes")
public class Cloth {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="price")
	private Double price;
	
	@Column(name="description")
	private String description;
	
	
	@ManyToOne
	@JoinColumn(name="brand_id", referencedColumnName = "id")
	private Brand brand;
	
	@OneToMany(mappedBy = "cloth")
	private List<Inventory> inventories;
	
	@ManyToOne
	@JoinColumn(name="category_id",referencedColumnName = "id")
	private Category category;
	
	@OneToMany(mappedBy = "cloth")
	@JsonIgnore
	private List<CartItem> cart;
	
	@OneToMany(mappedBy = "cloth")
	@JsonIgnore
	private List<OrderItem> orderItems;
	
	@OneToOne
	@JoinColumn(name="image_id",referencedColumnName = "id")
	private Image image;
	
}

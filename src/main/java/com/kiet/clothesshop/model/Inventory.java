package com.kiet.clothesshop.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventories")
public class Inventory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column(name = "quantity")
	private Integer quantity;
	
	@ManyToOne
	@JoinColumn(name = "cloth_id", referencedColumnName = "id")
	@JsonIgnore
	private Cloth cloth;
	
	@ManyToOne
	@JoinColumn(name="size_id",referencedColumnName = "id")
	private Size size;

	public Inventory(Integer quantity) {
		super();
		this.quantity = quantity;
	}
	
	
	
}

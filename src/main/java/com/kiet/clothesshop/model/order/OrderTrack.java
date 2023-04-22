package com.kiet.clothesshop.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "order_track")
public class OrderTrack {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column(name = "status", nullable = false)
	private String status;
	
	@OneToMany(mappedBy = "orderTrack")
	@JsonIgnore
	private List<Order> orders;
}

package com.kiet.clothesshop.model.order;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.kiet.clothesshop.model.user.User;
import com.kiet.clothesshop.untils.AppConstant;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column(name = "order_date", nullable = false)
	private Date orderDate;
	
	
	@Column(name = "deliver_method")
	private String deliverMethod;
	
	@Column(name = "deliver_cost")
	private Double deliverCost;
	
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.EAGER)
	private List<OrderItem> orderItems;	
	
	@ManyToOne
	@JoinColumn(name = "order_track_id", referencedColumnName = "id", nullable = false)
	private OrderTrack orderTrack;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@JsonIgnore
	private User user;
	
	public void addOrderItems(OrderItem orderItem) {
		
		if(this.orderItems == null) {
			orderItems = new ArrayList<>();
		}
		orderItems.add(orderItem);
	}
	
	public Double getTotalProductPrice() {
		Double total = 0.0;
		for (OrderItem orderItem : orderItems) {
			total += orderItem.getTotalPrice();
		}
		return total;
	}
	
	public Double getTotalPrice() {
		Double total = getTotalProductPrice();
		if(deliverMethod.equals(AppConstant.STANDARD)) {
			total = total + AppConstant.STANDARD_COST;
		}else if(deliverMethod.equals(AppConstant.FAST)) {
			total = total + AppConstant.FAST_COST;
		}else if(deliverMethod.equals(AppConstant.VERY_FAST)) {
			total = total + AppConstant.VERY_FAST_COST;
		}
		return total;
	}
	
	public int getNumProduct() {
		int total = 0;
		for (OrderItem orderItem : orderItems) {
			total += orderItem.getQuantity();
		}
		return total;
	}
}

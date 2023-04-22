package com.kiet.clothesshop.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="banners")
@Data
public class Banner {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="image_id",referencedColumnName = "id")
	private Image image;
}

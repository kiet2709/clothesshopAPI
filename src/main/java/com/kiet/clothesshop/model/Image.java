package com.kiet.clothesshop.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="images")
@Data
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="title")
	private String title;
	
	@Column(name="path")
	private String path;
}

package com.sneakerspick.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "gallery")
public class Gallery {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String url;

	@ManyToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Product product;

}

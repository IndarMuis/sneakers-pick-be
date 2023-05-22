package com.sneakerspick.domains;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Entity
@Table(name = "product", indexes = {@Index(name = "uk_name", columnList = "name")})
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false, unique = true)
	private String name;

	@Column(length = 50)
	private Double price;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(length = 30)
	private String tags;
	@ManyToMany
	@JoinTable(
			name = "product_category",
			joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
			inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")}
	)
	List<Category> categories;

	@OneToMany(mappedBy = "product")
	private List<Gallery> galleries;

}

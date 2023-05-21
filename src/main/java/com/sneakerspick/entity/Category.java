package com.sneakerspick.entity;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Entity
@Table(name = "category")
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ManyToMany(mappedBy = "categories")
	private List<Product> products;
}

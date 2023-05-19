package com.sneakerspick.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "product", indexes = {@Index(name = "uk_name", columnList = "name")})
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false, unique = true)
	private String name;

	@Column(length = 50)
	private String price;

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
	@ToString.Exclude
	List<Category> categories;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Product product = (Product) o;
		return getId() != null && Objects.equals(getId(), product.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}

package com.sneakerspick.domains;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "transaction_item")
public class TransactionItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer quantity;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private AppUser appUser;

	@OneToOne
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "transaction_id", referencedColumnName = "id")
	private Transaction transaction;

}

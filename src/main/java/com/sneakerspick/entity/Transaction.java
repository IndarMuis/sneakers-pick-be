package com.sneakerspick.entity;

import com.sneakerspick.enums.PaymentType;
import com.sneakerspick.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "transaction")
public class Transaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String address;

	private Double totalPrice;

	private Double shippingPrice;

	@Column(columnDefinition = "VARCHAR(15) DEFAULT 'PENDING'")
	@Enumerated(value = EnumType.STRING)
	private PaymentStatus status;

	@Column(columnDefinition = "VARCHAR(15) DEFAULT 'MANUAL'")
	@Enumerated(value = EnumType.STRING)
	private PaymentType payment;

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private AppUser appUser;

	@OneToMany(mappedBy = "transaction")
	private List<TransactionItem> transactionItems;

}

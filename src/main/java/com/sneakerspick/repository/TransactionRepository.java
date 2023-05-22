package com.sneakerspick.repository;

import com.sneakerspick.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Long, Transaction> {
}

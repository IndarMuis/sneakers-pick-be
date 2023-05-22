package com.sneakerspick.repository;

import com.sneakerspick.entity.TransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionItemRepository extends JpaRepository<Long, TransactionItem> {
}

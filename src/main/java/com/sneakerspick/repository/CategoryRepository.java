package com.sneakerspick.repository;

import com.sneakerspick.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Long, Category> {
}

package com.sneakerspick.repository;

import com.sneakerspick.domain.Product;
import com.sneakerspick.dto.response.ProductResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

//    @Query(
//            "SELECT p FROM Product p WHERE "
//    )
//    Optional<ProductResponse> findProductById(Long id);

}

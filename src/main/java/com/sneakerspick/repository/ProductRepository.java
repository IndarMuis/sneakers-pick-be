package com.sneakerspick.repository;

import com.sneakerspick.domain.Product;
import com.sneakerspick.dto.request.ProductSearchRequest;
import com.sneakerspick.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query(
            "SELECT DISTINCT " +
                    "new com.sneakerspick.dto.response.ProductResponse(p.id, p.name, p.tags, p.price, p.description, pc.name) FROM Product p " +
                    "INNER JOIN ProductCategory pc ON pc.id = p.productCategory.id " +
                    "WHERE " +
                    "p.name LIKE LOWER(CONCAT('%', :name, '%')) OR " +
                    "p.tags LIKE LOWER(CONCAT('%', :tags, '%')) OR " +
                    "pc.name LIKE LOWER(CONCAT('%', :category, '%'))"
    )
    Page<ProductResponse> findProductList(String name, String tags, String category, Pageable pageable);

}

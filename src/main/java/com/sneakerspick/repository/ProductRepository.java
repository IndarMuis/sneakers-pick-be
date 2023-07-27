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
            "SELECT p FROM Product p " +
                    "INNER JOIN ProductCategory pc ON p.productCategory.id = pc.id " +
                    "INNER JOIN ProductGallery pg ON p.id = pg.product.id " +
                    "WHERE " +
                    "p.name LIKE LOWER(CONCAT('%', :name, '%')) AND " +
                    "p.tags LIKE LOWER(CONCAT('%', :tags, '%')) "
    )
    public Page<ProductResponse> findProductList(Long id, String name, Double price, String tags, Pageable pageable);

}

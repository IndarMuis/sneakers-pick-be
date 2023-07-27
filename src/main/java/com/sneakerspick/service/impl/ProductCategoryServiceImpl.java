package com.sneakerspick.service.impl;

import com.sneakerspick.domain.Product;
import com.sneakerspick.domain.ProductCategory;
import com.sneakerspick.domain.ProductGallery;
import com.sneakerspick.dto.response.ProductCategoryResponse;
import com.sneakerspick.dto.response.ProductResponse;
import com.sneakerspick.repository.ProductCategoryRepository;
import com.sneakerspick.repository.ProductRepository;
import com.sneakerspick.service.ProductCategoryService;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public Page<ProductCategoryResponse> findAll(Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductCategory> categories = productCategoryRepository.findAll(pageable);
        List<ProductCategoryResponse> categoryResponseList = categories.getContent().stream().map(this::toProductCategoryResponse).toList();

        return new PageImpl<>(categoryResponseList, pageable, categories.getTotalElements());
    }

    @Override
    public ProductCategoryResponse findByName(String name) {
        return null;
    }

    private ProductCategoryResponse toProductCategoryResponse(ProductCategory productCategory) {
        ProductCategoryResponse productCategoryResponse = new ProductCategoryResponse();
        productCategoryResponse.setCategoryId(productCategory.getId());
        productCategoryResponse.setName(productCategory.getName());
        if (productCategory.getProducts().isEmpty()) {
            productCategoryResponse.setProducts(Collections.emptyList());
        } else {
            List<ProductResponse> productResponseList = productCategory.getProducts()
                            .stream().map(product ->
                        ProductResponse.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .price(product.getPrice())
                                .tags(product.getTags())
                                .description(product.getDescription())
                                //.galleries(product.getGalleries().stream().map(ProductGallery::getUrl).toList())
                                .build()
                    ).toList();

            productCategoryResponse.setProducts(productResponseList);
        }
        return productCategoryResponse;

    }

}

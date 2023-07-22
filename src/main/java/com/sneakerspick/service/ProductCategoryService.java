package com.sneakerspick.service;

import com.sneakerspick.dto.response.ProductCategoryResponse;
import org.springframework.data.domain.Page;

public interface ProductCategoryService {

    Page<ProductCategoryResponse> findAll(Integer page, Integer size);

    ProductCategoryResponse findByName(String name);

}

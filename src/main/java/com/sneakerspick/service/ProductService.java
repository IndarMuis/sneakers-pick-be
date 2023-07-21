package com.sneakerspick.service;

import com.sneakerspick.dto.request.ProductSearchRequest;
import com.sneakerspick.dto.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<ProductResponse> searchProduct(ProductSearchRequest request);

}

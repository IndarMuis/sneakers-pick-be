package com.sneakerspick.service.impl;

import com.sneakerspick.domain.Product;
import com.sneakerspick.dto.request.ProductSearchRequest;
import com.sneakerspick.dto.response.ProductResponse;
import com.sneakerspick.repository.ProductRepository;
import com.sneakerspick.service.ProductService;
import com.sneakerspick.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Predicates;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProduct(ProductSearchRequest request) {

        Specification<Product> productSpecification = (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.getName())) {
                predicates.add(builder.like(root.get("name"), "%" + request.getName() + "%"));
            }

            if (Objects.nonNull(request.getTags())) {
                predicates.add(builder.like(root.get("tags"), "%" + request.getTags() + "%"));
            }

            if (Objects.nonNull(request.getCategory())) {
                predicates.add(builder.like(root.get("category"), "%" + request.getCategory() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Product> products = productRepository.findAll(productSpecification, pageable);
        if (products.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found");
        } else {
            List<ProductResponse> productResponses = products.stream().map(this::toProductResponse).toList();
            return new PageImpl<>(productResponses, pageable, products.getTotalElements());
        }

    }

    @Override
    public ProductResponse findById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found");
        }


        return toProductResponse(product.get());
    }

    private ProductResponse toProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .tags(product.getTags())
                .category(product.getProductCategory().getName())
                .galleries(product.getGalleries().stream().map(gallery -> gallery.getUrl()).toList())
                .build();
    }
}

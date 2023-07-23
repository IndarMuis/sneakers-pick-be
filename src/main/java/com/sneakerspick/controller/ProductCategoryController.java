package com.sneakerspick.controller;

import com.sneakerspick.dto.response.PagingResponse;
import com.sneakerspick.dto.response.ProductCategoryResponse;
import com.sneakerspick.dto.response.WebResponse;
import com.sneakerspick.repository.ProductCategoryRepository;
import com.sneakerspick.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/products")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping(
            path = "/category",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ProductCategoryResponse>> findAllCategory(
            @RequestParam(name = "page", required = true, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = true, defaultValue = "5") Integer size
    ) {
        Page<ProductCategoryResponse> productCategory = productCategoryService.findAll(page, size);

        return WebResponse.<List<ProductCategoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .data(productCategory.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(productCategory.getNumber())
                        .totalPage(productCategory.getTotalPages())
                        .size(productCategory.getSize()).build())
                .build();
    }

}

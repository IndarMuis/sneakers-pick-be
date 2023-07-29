package com.sneakerspick.controller;

import com.sneakerspick.dto.request.ProductSearchRequest;
import com.sneakerspick.dto.response.PagingResponse;
import com.sneakerspick.dto.response.ProductResponse;
import com.sneakerspick.dto.response.WebResponse;
import com.sneakerspick.service.ProductService;
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
@RequestMapping(path = "/api/v1")
public class ProductController {

    private final ProductService productService;

    @GetMapping(
            path = "/products",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<ProductResponse>> search(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "tags", required = false) String tags,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "page", required = true, defaultValue = "0") Integer page,
            @RequestParam(name = "size", required = true, defaultValue = "10") Integer size
    ) {
        ProductSearchRequest productSearchRequest = ProductSearchRequest.builder()
                .name(name)
                .tags(tags)
                .category(category)
                .page(page)
                .size(size).build();

        Page<ProductResponse> productResponse = productService.searchProduct(productSearchRequest);
        return WebResponse.<List<ProductResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .data(productResponse.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(productResponse.getNumber())
                        .totalPage(productResponse.getTotalPages())
                        .build()
                )
                .build();
    }

}

package com.sneakerspick.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductSearchRequest {

    private String name;

    private String tags;

    private String category;

    @NotBlank
    private Integer size;

    @NotBlank
    private Integer page;

}

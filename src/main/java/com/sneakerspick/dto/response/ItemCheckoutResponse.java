package com.sneakerspick.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemCheckoutResponse {

    private Long id;

    private Long userId;

    private Long transactionId;

    private Long quantity;

    private ProductResponse product;

}

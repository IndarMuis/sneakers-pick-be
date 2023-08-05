package com.sneakerspick.dto.response;

import com.sneakerspick.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckoutResponse {

    private Long id;

    private Long userId;

    private String address;

    private Double totalPrice;

    private Double shippingPrice;

    private TransactionStatus status;

    private List<ItemCheckoutResponse> items;

}

package com.sneakerspick.dto.request;

import com.sneakerspick.enums.PaymentType;
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
public class CheckoutRequest {

    private List<ItemCheckoutRequest> items;

    private String address;

    private TransactionStatus status;

    private Long totalPrice;

    private Long shippingPrice;

}

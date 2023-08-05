package com.sneakerspick.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sneakerspick.enums.PaymentType;
import com.sneakerspick.enums.TransactionStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CheckoutRequest {

    @NotEmpty
    private List<ItemCheckoutRequest> items;

    @NotBlank
    private String address;

    @NotNull
    private Double totalPrice;

    @NotNull
//    shipping_price
    private Double shippingPrice;

    private PaymentType paymentType;

    private TransactionStatus transactionStatus;

}

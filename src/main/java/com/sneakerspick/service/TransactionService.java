package com.sneakerspick.service;

import com.sneakerspick.dto.request.CheckoutRequest;
import com.sneakerspick.dto.response.CheckoutResponse;

public interface TransactionService {

    CheckoutResponse checkout(CheckoutRequest request);

}

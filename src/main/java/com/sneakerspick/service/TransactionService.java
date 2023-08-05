package com.sneakerspick.service;

import com.sneakerspick.dto.request.CheckoutRequest;
import com.sneakerspick.dto.request.TransactionSearchRequest;
import com.sneakerspick.dto.response.CheckoutResponse;
import com.sneakerspick.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

public interface TransactionService {

    CheckoutResponse checkout(CheckoutRequest request);

    Page<TransactionResponse> getAllTransaction(TransactionSearchRequest request);

}

package com.sneakerspick.service.impl;

import com.sneakerspick.domain.TransactionItem;
import com.sneakerspick.dto.request.CheckoutRequest;
import com.sneakerspick.dto.response.CheckoutResponse;
import com.sneakerspick.repository.TransactionItemRepository;
import com.sneakerspick.repository.TransactionRepository;
import com.sneakerspick.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionItemRepository transactionItemRepository;

    @Override
    public CheckoutResponse checkout(CheckoutRequest request) {
        return null;
    }
}

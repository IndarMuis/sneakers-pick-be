package com.sneakerspick.service.impl;

import com.sneakerspick.domain.*;
import com.sneakerspick.dto.request.CheckoutRequest;
import com.sneakerspick.dto.response.*;
import com.sneakerspick.enums.PaymentType;
import com.sneakerspick.enums.TransactionStatus;
import com.sneakerspick.repository.ProductRepository;
import com.sneakerspick.repository.TransactionItemRepository;
import com.sneakerspick.repository.TransactionRepository;
import com.sneakerspick.repository.UserRepository;
import com.sneakerspick.service.TransactionService;
import com.sneakerspick.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionItemRepository transactionItemRepository;
    private final ProductRepository productRepository;
    private final ValidationService validationService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        validationService.validate(request);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) auth.getPrincipal();
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        Transaction transaction = new Transaction();
        transaction.setAddress(request.getAddress());
        transaction.setShippingPrice(request.getShippingPrice());
        transaction.setTotalPrice(request.getTotalPrice());
        transaction.setPaymentType(
                request.getPaymentType() != null ? request.getPaymentType() : PaymentType.MANUAL
        );
        transaction.setTransactionStatus(
                request.getTransactionStatus() != null ? request.getTransactionStatus() : TransactionStatus.PENDING
        );
        transaction.setUser(user);

        log.info("SAVE TRANSACTION");
        Transaction transactionSave = transactionRepository.save(transaction);

        List<ItemCheckoutResponse> items = new ArrayList<>();
        request.getItems().forEach((item) -> {
        log.info("SAVE TRANSACTION ITEM");
            TransactionItem transactionItem = new TransactionItem();
            Product product = productRepository.findById(item.getProductId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found")
            );
            transactionItem.setUser(user);
            transactionItem.setProduct(product);
            transactionItem.setQuantity(item.getQuantity());
            transactionItem.setTransaction(transaction);
            TransactionItem saveItems = transactionItemRepository.save(transactionItem);

            items.add(toItemCheckoutResponse(saveItems));
        });

        return CheckoutResponse.builder()
                .id(transactionSave.getId())
                .userId(user.getId())
                .address(transactionSave.getAddress())
                .shippingPrice(transactionSave.getShippingPrice())
                .totalPrice(transactionSave.getTotalPrice())
                .status(transactionSave.getTransactionStatus())
                .items(items)
                .build();
    }

    private ItemCheckoutResponse toItemCheckoutResponse(TransactionItem item) {
        ProductResponse product = ProductResponse.builder()
                .id(item.getProduct().getId())
                .name(item.getProduct().getName())
                .price(item.getProduct().getPrice())
                .tags(item.getProduct().getTags())
                .description(item.getProduct().getDescription())
                .category(item.getProduct().getProductCategory().getName())
                .galleries(
                        item.getProduct().getGalleries().stream().map(ProductGallery::getUrl).toList()
                )
                .build();
        return ItemCheckoutResponse.builder()
                .id(item.getId())
                .userId(item.getUser().getId())
                .product(product)
                .quantity(item.getQuantity())
                .transactionId(item.getTransaction().getId())
                .build();

    }

}

package com.sneakerspick.controller;

import com.sneakerspick.domain.Transaction;
import com.sneakerspick.dto.request.CheckoutRequest;
import com.sneakerspick.dto.request.TransactionSearchRequest;
import com.sneakerspick.dto.response.CheckoutResponse;
import com.sneakerspick.dto.response.PagingResponse;
import com.sneakerspick.dto.response.TransactionResponse;
import com.sneakerspick.dto.response.WebResponse;
import com.sneakerspick.enums.TransactionStatus;
import com.sneakerspick.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/api/v1")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(
            path = "/transactions",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<CheckoutResponse> checkout(@RequestBody CheckoutRequest request) {
        CheckoutResponse response = transactionService.checkout(request);

        return WebResponse.<CheckoutResponse>builder()
                .message("success")
                .code(HttpStatus.CREATED.value())
                .data(response)
                .build();
    }

    @GetMapping(
            path = "/transactions",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<List<TransactionResponse>> getAllTransaction(
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "transaction_status", required = false) Integer status,
            @RequestParam(name = "page", required = true, defaultValue = "1") Integer page,
            @RequestParam(name = "size", required = true, defaultValue = "10") Integer size
            ) {
        TransactionSearchRequest request = TransactionSearchRequest.builder()
                .id(id)
                .status(status)
                .page(page - 1)
                .size(size).build();

        Page<TransactionResponse> transactionResponses = transactionService.getAllTransaction(request);
        return WebResponse.<List<TransactionResponse>>builder()
                .code(HttpStatus.OK.value())
                .message("success")
                .data(transactionResponses.getContent())
                .paging(PagingResponse.builder()
                        .currentPage(transactionResponses.getNumber() + 1)
                        .totalPage(transactionResponses.getTotalPages()).build())
                .build();
    }

}

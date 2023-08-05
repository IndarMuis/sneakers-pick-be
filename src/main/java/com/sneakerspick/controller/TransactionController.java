package com.sneakerspick.controller;

import com.sneakerspick.dto.request.CheckoutRequest;
import com.sneakerspick.dto.response.CheckoutResponse;
import com.sneakerspick.dto.response.WebResponse;
import com.sneakerspick.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/api/v1")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(
            path = "/transaction",
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

}

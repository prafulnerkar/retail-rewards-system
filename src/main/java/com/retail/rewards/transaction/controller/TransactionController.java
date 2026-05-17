package com.retail.rewards.transaction.controller;

import com.retail.rewards.common.PageResponse;
import com.retail.rewards.transaction.dto.TransactionCreateRequest;
import com.retail.rewards.transaction.dto.TransactionResponse;
import com.retail.rewards.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Transaction REST controller.
 */
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Get paginated transactions.
     *
     * @param pageable pageable
     * @return transaction page
     */
    @GetMapping
    public ResponseEntity<PageResponse<TransactionResponse>> getTransactions(
            Pageable pageable) {

        return ResponseEntity.ok(
                transactionService.getAllTransactions(pageable)
        );

    }

    /**
     * Create transaction.
     *
     * @param request transaction request
     * @return created transaction
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionCreateRequest request) {

        TransactionResponse response = transactionService.createTransaction(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
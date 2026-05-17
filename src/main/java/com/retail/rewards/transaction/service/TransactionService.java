package com.retail.rewards.transaction.service;

import com.retail.rewards.common.PageResponse;
import com.retail.rewards.transaction.dto.TransactionCreateRequest;
import com.retail.rewards.transaction.dto.TransactionResponse;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;

/**
 * Transaction recording use cases.
 */
public interface TransactionService {

    /**
     * Records a transaction and calculates reward points.
     *
     * @param request transaction payload
     * @return transaction response
     */
    TransactionResponse createTransaction(@Valid TransactionCreateRequest request);


    /**
     * Lists transactions with paging and sorting.
     *
     * @param pageable page request
     * @return page response
     */

    PageResponse<TransactionResponse> getAllTransactions(Pageable pageable);


}

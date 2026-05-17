package com.retail.rewards.transaction.service;

import com.retail.rewards.common.PageResponse;
import com.retail.rewards.common.util.RewardCalculator;
import com.retail.rewards.customer.entity.CustomerEntity;
import com.retail.rewards.customer.repository.CustomerRepository;
import com.retail.rewards.exception.ForbiddenException;
import com.retail.rewards.exception.ResourceNotFoundException;
import com.retail.rewards.security.UserPrincipal;
import com.retail.rewards.transaction.dto.TransactionCreateRequest;
import com.retail.rewards.transaction.dto.TransactionRequestDto;
import com.retail.rewards.transaction.dto.TransactionResponse;
import com.retail.rewards.transaction.dto.TransactionResponseDto;
import com.retail.rewards.transaction.entity.CustomerTransactionEntity;
import com.retail.rewards.transaction.mapper.TransactionMapper;
import com.retail.rewards.transaction.repository.TransactionRepository;
import com.retail.rewards.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Default transaction service implementation.
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;




    @Transactional
    @Override
    public TransactionResponse createTransaction(TransactionCreateRequest request) {

        UserPrincipal principal = SecurityUtil.currentPrincipal();
        CustomerEntity customer = resolveCustomer(request.getCustomerId(), principal);

        LocalDateTime transactionDate = request.getTransactionDate() != null ? request.getTransactionDate() : LocalDateTime.now();
        long rewardPoints = RewardCalculator.calculate(request.getAmount());

        CustomerTransactionEntity transaction = new CustomerTransactionEntity();
        transaction.setCustomer(customer);
        transaction.setTransactionReference(UUID.randomUUID().toString());
        transaction.setAmount(request.getAmount());
        transaction.setRewardPoints(rewardPoints);
        transaction.setTransactionDate(transactionDate);
        transaction.setDescription(request.getDescription());

        return TransactionMapper.toResponse(transactionRepository.save(transaction));



    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<TransactionResponse> getAllTransactions(Pageable pageable) {
        Page<CustomerTransactionEntity> page = transactionRepository.findAll(pageable);
        return PageResponse.<TransactionResponse>builder()
                .content(page.map(TransactionMapper::toResponse).getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .sort(pageable.getSort().toString())
                .build();



    }


    private CustomerEntity resolveCustomer(Long customerId, UserPrincipal principal) {
        if (customerId != null) {
            if (principal.getRoles().contains("ROLE_CUSTOMER")) {
                CustomerEntity linked = customerRepository.findByEmail(principal.getEmail())
                        .orElseThrow(() -> new ForbiddenException("Customer profile not linked to authenticated user"));
                if (!linked.getId().equals(customerId)) {
                    throw new ForbiddenException("Customer can only record transactions for its own profile");
                }
            }
            return customerRepository.findById(customerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));
        }

        if (principal.getRoles().contains("ROLE_CUSTOMER")) {
            return customerRepository.findByEmail(principal.getEmail())
                    .orElseThrow(() -> new ForbiddenException("Customer profile not linked to authenticated user"));
        }

        throw new ForbiddenException("customerId is required for non-customer principals");
    }
}

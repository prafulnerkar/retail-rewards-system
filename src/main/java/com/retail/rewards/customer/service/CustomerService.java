package com.retail.rewards.customer.service;

import com.retail.rewards.customer.dto.CustomerRequestDto;
import com.retail.rewards.customer.dto.CustomerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Customer service contract.
 */
public interface CustomerService {

    /**
     * Fetch all customers with pagination.
     *
     * @param pageable pageable configuration
     * @return customer page
     */
    Page<CustomerResponseDto> getAllCustomers(Pageable pageable);

    /**
     * Create customer.
     *
     * @param request customer request
     * @return created customer
     */
    CustomerResponseDto createCustomer(CustomerRequestDto request);

    /**
     * Fetch customer by id.
     *
     * @param id customer id
     * @return customer response
     */
    CustomerResponseDto getCustomerById(Long id);
}
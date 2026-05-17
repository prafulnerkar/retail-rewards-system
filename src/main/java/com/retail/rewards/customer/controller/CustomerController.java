package com.retail.rewards.customer.controller;

import com.retail.rewards.customer.dto.CustomerRequestDto;
import com.retail.rewards.customer.dto.CustomerResponseDto;
import com.retail.rewards.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Customer REST controller.
 */
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final CustomerService customerService;

    /**
     * Get paginated customers.
     *
     * @param pageable pageable
     * @return customer page
     */
    @GetMapping
    public ResponseEntity<Page<CustomerResponseDto>> getCustomers(Pageable pageable) {

        return ResponseEntity.ok(
                customerService.getAllCustomers(pageable)
        );
    }

    /**
     * Create customer.
     *
     * @param request customer request
     * @return created customer
     */
    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(
            @Valid @RequestBody CustomerRequestDto request) {

        CustomerResponseDto response =
                customerService.createCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Get customer by id.
     *
     * @param id customer id
     * @return customer
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomer(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                customerService.getCustomerById(id)
        );
    }
}
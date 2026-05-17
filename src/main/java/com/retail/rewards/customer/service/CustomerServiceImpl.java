package com.retail.rewards.customer.service;

import com.retail.rewards.customer.dto.CustomerRequestDto;
import com.retail.rewards.customer.dto.CustomerResponseDto;
import com.retail.rewards.customer.entity.CustomerEntity;
import com.retail.rewards.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Customer service implementation.
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Page<CustomerResponseDto> getAllCustomers(Pageable pageable) {

        return customerRepository.findAll(pageable)
                .map(this::mapToDto);
    }

    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto request) {

        CustomerEntity customer = CustomerEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .build();

        CustomerEntity savedCustomer = customerRepository.save(customer);

        return mapToDto(savedCustomer);
    }

    @Override
    public CustomerResponseDto getCustomerById(Long id) {

        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found"));

        return mapToDto(customer);
    }

    /**
     * Entity to DTO mapper.
     */
    private CustomerResponseDto mapToDto(CustomerEntity customer) {

        return CustomerResponseDto.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .build();
    }
}
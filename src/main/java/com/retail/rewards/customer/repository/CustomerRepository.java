package com.retail.rewards.customer.repository;

import com.retail.rewards.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for customer profiles.
 */
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    /**
     * Finds a customer by email.
     *
     * @param email email address
     * @return matching customer if present
     */
    Optional<CustomerEntity> findByEmail(String email);

    /**
     * Checks whether a customer with the email exists.
     *
     * @param email email address
     * @return true when the email exists
     */
    boolean existsByEmail(String email);
}

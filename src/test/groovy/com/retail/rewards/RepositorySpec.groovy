package com.retail.rewards.repository

import com.retail.rewards.common.enums.CustomerStatus
import com.retail.rewards.customer.entity.CustomerEntity
import com.retail.rewards.customer.repository.CustomerRepository
import com.retail.rewards.transaction.entity.CustomerTransactionEntity
import com.retail.rewards.transaction.repository.TransactionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import java.time.LocalDateTime

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RepositorySpec extends Specification {

    @Autowired
    CustomerRepository customerRepository

    @Autowired
    TransactionRepository transactionRepository

    def "saves customer and aggregates rewards by date range"() {

        given:
        LocalDateTime now = LocalDateTime.now()

        def customer = CustomerEntity.builder()
                .firstName("Alice")
                .lastName("Walker")
                .email("alice@example.com")
                .phone("9999999999")
                .status(CustomerStatus.ACTIVE)
                .build()

        customer = customerRepository.save(customer)

        def transaction = new CustomerTransactionEntity();
        transaction.setCustomer(customer);
        transaction.setAmount(new BigDecimal("120"));
        transaction.setRewardPoints(510l);
        transaction.setTransactionDate(now);
        transaction.setDescription("purchase");

        when:
        Long total = transactionRepository.sumRewardPoints(
                4,
                now.minusDays(1),
                now.plusDays(1)
        )

        then:
        total == transaction.getRewardPoints();
    }
}
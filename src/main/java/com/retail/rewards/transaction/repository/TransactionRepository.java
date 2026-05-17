package com.retail.rewards.transaction.repository;

import com.retail.rewards.transaction.entity.CustomerTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for customer transactions and reward aggregation.
 */
public interface TransactionRepository extends JpaRepository<CustomerTransactionEntity, Long> {

    /**
     * Returns a paged list of transactions for a customer.
     *
     * @param customerId customer identifier
     * @param pageable paging configuration
     * @return page of transactions
     */
    Page<CustomerTransactionEntity> findByCustomer_Id(Long customerId, Pageable pageable);

    /**
     * Aggregates reward points over a date range for a customer.
     *
     * @param customerId customer identifier
     * @param from start timestamp
     * @param to end timestamp
     * @return aggregated total points
     */
    @Query("select coalesce(sum(t.rewardPoints), 0) from CustomerTransactionEntity t " +
            "where t.customer.id = :customerId and t.transactionDate between :from and :to")
    Long sumRewardPoints(@Param("customerId") Long customerId,
                         @Param("from") LocalDateTime from,
                         @Param("to") LocalDateTime to);

    /**
     * Groups reward points by month using the database engine.
     *
     * @param customerId customer identifier
     * @param from start timestamp
     * @param to end timestamp
     * @return list of [month_start, total_points]
     */
    @Query(value = "select date_trunc('month', transaction_date) as month_start, coalesce(sum(reward_points), 0) as total_points " +
            "from customer_transactions " +
            "where customer_id = :customerId and transaction_date between :from and :to " +
            "group by month_start order by month_start",
            nativeQuery = true)
    List<Object[]> aggregateMonthlyRewardPoints(@Param("customerId") Long customerId,
                                                @Param("from") LocalDateTime from,
                                                @Param("to") LocalDateTime to);
}

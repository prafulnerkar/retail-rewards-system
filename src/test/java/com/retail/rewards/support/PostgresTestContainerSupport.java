
package com.retail.rewards.support;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import spock.lang.Specification;

/**
 * Base specification that manages a reusable PostgreSQL Testcontainer.
 */
public abstract class PostgresTestContainerSupport extends Specification {

    protected static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("retail_rewards_test")
            .withUsername("test")
            .withPassword("test");

    static {
        POSTGRES.start();
    }

    /**
     * Registers container properties for Spring.
     *
     * @param registry property registry
     */
    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES::getDriverClassName);
    }
}

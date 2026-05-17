package com.retail.rewards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application entry point for the Retail Rewards modular monolith.
 */
@SpringBootApplication
public class RewardsApplication {

    /**
     * Starts the Spring Boot application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(RewardsApplication.class, args);
        System.out.println("Application is started.....!!!");

    }
}

package com.retail.rewards.auth.repository;

import com.retail.rewards.auth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for user accounts.
 */
public interface    UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Looks up a user by email.
     *
     * @param email email address
     * @return matching user if present
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Checks whether a user with the email exists.
     *
     * @param email email address
     * @return true when the email exists
     */
    boolean existsByEmail(String email);
}

package com.retail.rewards.auth.repository;

import com.retail.rewards.auth.entity.RoleEntity;
import com.retail.rewards.common.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for authorization roles.
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    /**
     * Finds a role by name.
     *
     * @param name role name
     * @return matching role if present
     */
    Optional<RoleEntity> findByName(RoleName name);
}

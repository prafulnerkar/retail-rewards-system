package com.retail.rewards.security;

import com.retail.rewards.auth.entity.RoleEntity;
import com.retail.rewards.auth.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Spring Security principal backed by a UserEntity.
 */
@Getter
public class UserPrincipal implements UserDetails {

    private final Long userId;
    private final String email;
    private final String fullName;
    private final String password;
    private final boolean enabled;
    private final Set<String> roles;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long userId, String email, String fullName, String password, boolean enabled, Set<String> roles) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
        this.authorities = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    /**
     * Builds a principal from a persistent user.
     *
     * @param user user entity
     * @return principal
     */
    public static UserPrincipal from(UserEntity user) {
        Set<String> roles = user.getRoles().stream().map(RoleEntity::getName).map(Enum::name).collect(Collectors.toSet());
        return new UserPrincipal(user.getId(), user.getEmail(), user.getFullName(), user.getPassword(), user.isEnabled(), roles);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}

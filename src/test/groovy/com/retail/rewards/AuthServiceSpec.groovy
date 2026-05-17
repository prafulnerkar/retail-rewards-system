package com.retail.rewards.auth.service

import com.retail.rewards.auth.dto.LoginRequest
import com.retail.rewards.auth.dto.RegisterRequest
import com.retail.rewards.auth.entity.RoleEntity
import com.retail.rewards.auth.entity.UserEntity
import com.retail.rewards.auth.repository.RoleRepository
import com.retail.rewards.auth.repository.UserRepository
import com.retail.rewards.common.enums.RoleName
import com.retail.rewards.customer.repository.CustomerRepository
import com.retail.rewards.security.JwtTokenProvider
import com.retail.rewards.security.UserPrincipal
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

class AuthServiceSpec extends Specification {

    UserRepository userRepository = Mock()
    RoleRepository roleRepository = Mock()
    CustomerRepository customerRepository = Mock()
    PasswordEncoder passwordEncoder = Mock()
    AuthenticationManager authenticationManager = Mock()
    JwtTokenProvider jwtTokenProvider = Mock()

    AuthService service = new AuthServiceImpl(userRepository, roleRepository, customerRepository, passwordEncoder, authenticationManager, jwtTokenProvider)

    def "register creates user and customer and returns token"() {
        given:
        def request = new RegisterRequest(firstName: "Alice", lastName: "Walker", email: "alice@example.com", password: "Password@123", phone: "999")
        def role = new RoleEntity(name: RoleName.ROLE_CUSTOMER)
        def savedUser = new UserEntity(id: 1L, email: "alice@example.com", fullName: "Alice Walker", password: "encoded", roles: [role] as Set, enabled: true)
        userRepository.existsByEmail("alice@example.com") >> false
        customerRepository.existsByEmail("alice@example.com") >> false
        roleRepository.findByName(RoleName.ROLE_CUSTOMER) >> Optional.of(role)
        passwordEncoder.encode("Password@123") >> "encoded"
        userRepository.save(_ as UserEntity) >> { UserEntity u -> u.id = 1L; u.roles = [role] as Set; u }
        customerRepository.save(_ as com.retail.rewards.customer.entity.CustomerEntity) >> { com.retail.rewards.customer.entity.CustomerEntity c -> c }

        and:
        jwtTokenProvider.generateToken(_ as Authentication) >> "jwt"
        jwtTokenProvider.getExpirationInSecondsValue() >> 86400L

        when:
        def response = service.register(request)

        then:
        response.token == "jwt"
        response.email == "alice@example.com"
        response.roles.contains("ROLE_CUSTOMER")
    }

    def "login authenticates user and returns token"() {
        given:
        def request = new LoginRequest(email: "alice@example.com", password: "Password@123")
        def role = new RoleEntity(name: RoleName.ROLE_CUSTOMER)
        def user = new UserEntity(id: 1L, email: "alice@example.com", fullName: "Alice Walker", password: "encoded", roles: [role] as Set, enabled: true)
        def principal = UserPrincipal.from(user)
        def auth = new UsernamePasswordAuthenticationToken(principal, null, principal.authorities)
        authenticationManager.authenticate(_ as UsernamePasswordAuthenticationToken) >> auth
        jwtTokenProvider.generateToken(auth) >> "jwt"
        jwtTokenProvider.getExpirationInSecondsValue() >> 86400L

        when:
        def response = service.login(request)

        then:
        response.token == "jwt"
        response.userId == 1L
        response.email == "alice@example.com"
    }
}

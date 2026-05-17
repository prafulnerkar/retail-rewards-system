
package com.retail.rewards.transaction.service

import com.retail.rewards.auth.entity.RoleEntity
import com.retail.rewards.auth.entity.UserEntity
import com.retail.rewards.customer.entity.CustomerEntity
import com.retail.rewards.customer.repository.CustomerRepository
import com.retail.rewards.common.enums.RoleName
import com.retail.rewards.security.UserPrincipal
import com.retail.rewards.transaction.dto.TransactionCreateRequest
import com.retail.rewards.transaction.entity.CustomerTransactionEntity
import com.retail.rewards.transaction.repository.TransactionRepository
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import spock.lang.Specification

class TransactionServiceSpec extends Specification {

    TransactionRepository transactionRepository = Mock()
    CustomerRepository customerRepository = Mock()

    TransactionService service = new TransactionServiceImpl(transactionRepository, customerRepository)

    def setup() {
        SecurityContextHolder.clearContext()
    }

    def cleanup() {
        SecurityContextHolder.clearContext()
    }

    def "creates transaction and calculates rewards"() {
        given:
        def role = new RoleEntity(name: RoleName.ROLE_CUSTOMER)
        def user = new UserEntity(id: 10L, email: "alice@example.com", fullName: "Alice Walker", password: "x", roles: [role] as Set, enabled: true)
        def principal = UserPrincipal.from(user)
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, principal.authorities))

        def customer = new CustomerEntity(id: 1L, firstName: "Alice", lastName: "Walker", email: "alice@example.com")
        customerRepository.findByEmail("alice@example.com") >> Optional.of(customer)

        def request = new TransactionCreateRequest(customerId: null, amount: new BigDecimal("120"), description: "purchase")
        transactionRepository.save(_ as CustomerTransactionEntity) >> { CustomerTransactionEntity tx ->
            tx.id = 55L
            tx.customer = customer
            tx
        }

        when:
        def response = service.createTransaction(request)

        then:
        response.rewardPoints == 90L
        response.customerId == 1L
        response.id == 55L
    }

    def "lists transactions with pagination"() {
        given:
        transactionRepository.findAll(_ as org.springframework.data.domain.Pageable) >> new PageImpl([])

        expect:
        service.list(PageRequest.of(0, 20)).totalElements == 0
    }
}

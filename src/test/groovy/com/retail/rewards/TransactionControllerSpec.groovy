package com.retail.rewards.transaction.controller

import com.retail.rewards.transaction.service.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerSpec extends Specification {

    @Autowired MockMvc mockMvc
    @MockBean TransactionService transactionService

    def "rejects invalid transaction amount"() {
        expect:
        mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content('{ "customerId": 1, "amount": 0, "description": "invalid" }'))
                .andExpect(status().isBadRequest())
    }
}

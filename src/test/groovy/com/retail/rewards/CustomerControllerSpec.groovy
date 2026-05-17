
package com.retail.rewards.customer.controller

import com.retail.rewards.common.PageResponse
import com.retail.rewards.customer.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(CustomerController)
@AutoConfigureMockMvc(addFilters = false)
class CustomerControllerSpec extends Specification {

    @Autowired MockMvc mockMvc
    @MockBean CustomerService customerService

    def "list customers returns ok"() {
        given:
        customerService.list(_ as org.springframework.data.domain.Pageable) >> PageResponse.builder().content([]).page(0).size(20).totalElements(0).totalPages(0).sort("createdAt,desc").build()

        expect:
        mockMvc.perform(get("/customers").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
    }
}

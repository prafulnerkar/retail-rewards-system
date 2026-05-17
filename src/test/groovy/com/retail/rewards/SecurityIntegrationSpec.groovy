
package com.retail.rewards.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.retail.rewards.auth.dto.LoginRequest
import com.retail.rewards.auth.dto.RegisterRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Security integration tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityIntegrationSpec extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper objectMapper

    def "public login endpoint is accessible without token"() {

        given:
        def request = new LoginRequest(
                email: "admin@retail.com",
                password: "password"
        )

        expect:
        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk())
    }

    def "protected endpoints reject missing token"() {

        expect:
        mockMvc.perform(
                get("/customers")
        ).andExpect(status().isUnauthorized())
    }

    def "invalid jwt token is rejected"() {

        expect:
        mockMvc.perform(
                get("/customers")
                        .header("Authorization", "Bearer invalid.token.value")
        ).andExpect(status().isUnauthorized())
    }

    def "register endpoint returns success response"() {

        given:
        def request = new RegisterRequest(
                firstName: "Test",
                lastName: "User",
                email: "vihan@gmail.com",
                password: "vihan@123",
                phone: "9999999999"
        )

        expect:
        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk())
    }

}

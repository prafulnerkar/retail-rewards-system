
package com.retail.rewards.rewards.controller

import com.retail.rewards.rewards.dto.MonthlyRewardResponse
import com.retail.rewards.rewards.service.RewardsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import java.math.BigDecimal
import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(RewardsController)
@AutoConfigureMockMvc(addFilters = false)
class RewardsControllerSpec extends Specification {

    @Autowired MockMvc mockMvc
    @MockBean RewardsService rewardsService

    def "monthly rewards endpoint returns ok"() {
        given:
        rewardsService.monthlyRewards(1L, LocalDate.parse("2026-01-01"), LocalDate.parse("2026-05-31")) >> [MonthlyRewardResponse.builder().yearMonth("2026-01").rewardPoints(BigDecimal.TEN).build()]

        expect:
        mockMvc.perform(get("/rewards/1/monthly?from=2026-01-01&to=2026-05-31").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
    }
}

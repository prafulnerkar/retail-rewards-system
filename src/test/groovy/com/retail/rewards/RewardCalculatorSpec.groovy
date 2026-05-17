package com.retail.rewards.common.util

import spock.lang.Specification

class RewardCalculatorSpec extends Specification {

    def "calculates rewards according to business rules"() {
        expect:
        RewardCalculator.calculate(new BigDecimal("120")) == 90L
        RewardCalculator.calculate(new BigDecimal("100")) == 50L
        RewardCalculator.calculate(new BigDecimal("50")) == 0L
    }
}

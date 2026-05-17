package com.retail.rewards.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Reward point calculation utility.
 */
public final class RewardCalculator {

    private static final BigDecimal HUNDRED = new BigDecimal("100");
    private static final BigDecimal FIFTY = new BigDecimal("50");

    private RewardCalculator() {
    }

    /**
     * Calculates reward points for a transaction.
     * Rules:
     * <ul>
     *   <li>2 points for every dollar above 100</li>
     *   <li>1 point for every dollar between 50 and 100</li>
     * </ul>
     *
     * @param amount transaction amount
     * @return reward points as whole number
     */
    public static long calculate(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return 0L;
        }

        BigDecimal overHundred = amount.subtract(HUNDRED).max(BigDecimal.ZERO);
        BigDecimal betweenFiftyAndHundred = amount.min(HUNDRED).subtract(FIFTY).max(BigDecimal.ZERO);

        BigDecimal points = overHundred.multiply(new BigDecimal("2"))
                .add(betweenFiftyAndHundred);

        return points.setScale(0, RoundingMode.DOWN).longValueExact();
    }
}

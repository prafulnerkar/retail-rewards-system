package com.retail.rewards.common.util;

import com.retail.rewards.common.DateRange;
import com.retail.rewards.exception.BadRequestException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 * Utilities for normalizing date ranges and month calculations.
 */
public final class DateRangeUtil {

    private DateRangeUtil() {
    }

    /**
     * Resolves an optional date range. When absent, a rolling 12-month window ending in the current month is used.
     *
     * @param from optional start date
     * @param to optional end date
     * @return normalized date range
     */
    public static DateRange resolve(LocalDate from, LocalDate to) {
        LocalDate actualTo = to != null ? to : LocalDate.now();
        LocalDate actualFrom = from != null ? from : actualTo.minusMonths(11).withDayOfMonth(1);

        if (actualFrom.isAfter(actualTo)) {
            throw new BadRequestException("Start date must be before or equal to end date");
        }

        return new DateRange(actualFrom.atStartOfDay(), actualTo.plusDays(1).atStartOfDay().minusNanos(1));
    }

    /**
     * Resolves a date range from ISO dates with a default rolling 12-month window.
     *
     * @param from optional start date
     * @param to optional end date
     * @return normalized date range
     */
    public static DateRange resolveForReport(LocalDate from, LocalDate to) {
        return resolve(from, to);
    }

    /**
     * Converts the supplied local date to the first instant of its month.
     *
     * @param date the date
     * @return month start
     */
    public static LocalDateTime monthStart(LocalDate date) {
        return YearMonth.from(date).atDay(1).atStartOfDay();
    }
}

package com.retail.rewards.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Normalized date range for reports.
 */
@Data
@AllArgsConstructor
public class DateRange {
    private LocalDateTime start;
    private LocalDateTime end;
}

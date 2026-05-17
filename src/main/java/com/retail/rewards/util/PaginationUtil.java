package com.retail.rewards.util;

import org.springframework.data.domain.Sort;

/**
 * Utility methods for parsing API pagination inputs.
 */
public final class PaginationUtil {

    private PaginationUtil() {
    }

    /**
     * Parses a sort expression in the form `field,dir`.
     *
     * @param sort sort expression
     * @param defaultField fallback field
     * @return sort configuration
     */
    public static Sort parseSort(String sort, String defaultField) {
        if (sort == null || sort.trim().isEmpty()) {
            return Sort.by(Sort.Direction.DESC, defaultField);
        }
        String[] parts = sort.split(",");
        if (parts.length == 2) {
            return Sort.by(Sort.Direction.fromString(parts[1].trim()), parts[0].trim());
        }
        return Sort.by(Sort.Direction.DESC, sort.trim());
    }
}

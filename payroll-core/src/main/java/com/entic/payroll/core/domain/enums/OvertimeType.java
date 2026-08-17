package com.entic.payroll.core.domain.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public enum OvertimeType {

    DAY_OVERTIME("0.25", true, "Day Overtime"),
    NIGHT_OVERTIME("0.75", true, "Night Overtime"),
    DAY_SUNDAY_HOLIDAY_OVERTIME("1.00", true, "Day Sunday/Holiday Overtime"),
    NIGHT_SUNDAY_HOLIDAY_OVERTIME("1.50", true, "Night Sunday/Holiday Overtime"),
    NIGHT_SURCHARGE("0.35", false, "Night Surcharge"),
    SUNDAY_HOLIDAY_SURCHARGE("0.75", false, "Sunday/Holiday Surcharge"),
    REGULAR_SUNDAY_HOLIDAY_SURCHARGE("1.00", false, "Regular Sunday/Holiday Surcharge");

    private static final Logger log = LoggerFactory.getLogger(OvertimeType.class);

    private final BigDecimal surcharge;
    private final boolean overtime;
    private final String label;

    OvertimeType(String surcharge, boolean overtime, String label) {
        this.surcharge = new BigDecimal(surcharge);
        this.overtime = overtime;
        this.label = label;
    }

    public BigDecimal getSurcharge() {
        return surcharge;
    }

    public boolean isOvertime() {
        return overtime;
    }

    public String getLabel() {
        return label;
    }
}

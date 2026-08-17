package com.entic.payroll.core.domain.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public enum RiskLevel {

    ONE("0.00522", "Risk I - Minimum"),
    TWO("0.01044", "Risk II - Low"),
    THREE("0.02436", "Risk III - Medium"),
    FOUR("0.0435", "Risk IV - High"),
    FIVE("0.0696", "Risk V - Maximum");

    private static final Logger log = LoggerFactory.getLogger(RiskLevel.class);

    private final BigDecimal rate;
    private final String label;

    RiskLevel(String rate, String label) {
        this.rate = new BigDecimal(rate);
        this.label = label;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public String getLabel() {
        return label;
    }
}

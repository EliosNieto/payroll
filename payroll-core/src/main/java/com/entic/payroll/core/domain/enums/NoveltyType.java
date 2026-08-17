package com.entic.payroll.core.domain.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum NoveltyType {

    DISABILITY,
    MATERNITY_LEAVE,
    PATERNITY_LEAVE,
    VACATION,
    UNPAID_LEAVE;

    private static final Logger log = LoggerFactory.getLogger(NoveltyType.class);
}

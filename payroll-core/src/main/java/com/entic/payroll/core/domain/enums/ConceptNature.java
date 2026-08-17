package com.entic.payroll.core.domain.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ConceptNature {

    EARNINGS,
    DEDUCTION,
    EMPLOYER_CONTRIBUTION,
    PROVISION;

    private static final Logger log = LoggerFactory.getLogger(ConceptNature.class);
}

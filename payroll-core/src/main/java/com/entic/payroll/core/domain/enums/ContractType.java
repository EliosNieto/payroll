package com.entic.payroll.core.domain.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum ContractType {

    FIXED_TERM("01", "Fixed Term"),
    INDEFINITE_TERM("02", "Indefinite Term"),
    WORK_OR_LABOR("03", "Work or Labor"),
    APPRENTICESHIP("04", "Apprenticeship"),
    TEMPORARY("05", "Temporary Work");

    private static final Logger log = LoggerFactory.getLogger(ContractType.class);

    private final String code;
    private final String label;

    ContractType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static ContractType fromCode(String code) {
        for (ContractType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown contract type code: " + code);
    }
}

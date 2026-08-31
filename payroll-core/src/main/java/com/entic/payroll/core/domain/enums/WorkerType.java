package com.entic.payroll.core.domain.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum WorkerType {

    DEPENDENT("01", "Dependent Worker"),
    INDEPENDENT("02", "Independent Worker"),
    DOMESTIC_WORKER("03", "Domestic Worker"),
    STUDENT_TRAINEE("04", "Student Trainee");

    private static final Logger log = LoggerFactory.getLogger(WorkerType.class);

    private final String code;
    private final String label;

    WorkerType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static WorkerType fromCode(String code) {
        for (WorkerType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown worker type code: " + code);
    }
}
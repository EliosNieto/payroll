package com.entic.payroll.core.domain.enums;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum DocumentType {

    CITIZENSHIP_CARD("CC", "Citizenship Card"),
    FOREIGNER_ID("CE", "Foreigner ID"),
    TAX_ID("NIT", "Tax ID"),
    PASSPORT("PA", "Passport"),
    MINOR_ID("TI", "Minor ID");

    private static final Logger log = LoggerFactory.getLogger(DocumentType.class);

    private final String code;
    private final String label;

    DocumentType(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static DocumentType fromCode(String code) {
        for (DocumentType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown document type code: " + code);
    }
}